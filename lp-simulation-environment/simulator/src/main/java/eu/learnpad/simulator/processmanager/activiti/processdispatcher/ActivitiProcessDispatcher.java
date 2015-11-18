/**
 *
 */
package eu.learnpad.simulator.processmanager.activiti.processdispatcher;

/*
 * #%L
 * LearnPAd Simulator
 * %%
 * Copyright (C) 2014 - 2015 Linagora
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import eu.learnpad.sim.rest.data.ProcessInstanceData;
import eu.learnpad.simulator.IProcessEventReceiver;
import eu.learnpad.simulator.datastructures.LearnPadTask;
import eu.learnpad.simulator.datastructures.document.LearnPadDocument;
import eu.learnpad.simulator.datastructures.document.LearnPadDocumentField;
import eu.learnpad.simulator.processmanager.AbstractProcessDispatcher;
import eu.learnpad.simulator.processmanager.ITaskRouter;
import eu.learnpad.simulator.processmanager.ITaskValidator;
import eu.learnpad.simulator.processmanager.activiti.ActivitiProcessManager;
import eu.learnpad.simulator.utils.BPMNExplorer;

/**
 *
 * @author Tom Jorquera - Linagora
 *
 */
public class ActivitiProcessDispatcher extends AbstractProcessDispatcher {

	private final TaskService taskService;
	private final RuntimeService runtimeService;
	private final HistoryService historyService;
	private final BPMNExplorer explorer;

	private final Set<String> registeredWaitingTasks = new HashSet<String>();

	private boolean processEndNotified = false;

	public ActivitiProcessDispatcher(
			ProcessInstanceData processInstanceData,
			ActivitiProcessManager processManager,
			IProcessEventReceiver processEventReceiver,
			TaskService taskService,
			RuntimeService runtimeService,
			HistoryService historyService,
			ITaskRouter router,
			ITaskValidator<Map<String, Object>, Map<String, Object>> taskValidator,
			BPMNExplorer explorer) {
		super(processInstanceData, processManager, processEventReceiver,
				router, taskValidator);
		this.taskService = taskService;
		this.runtimeService = runtimeService;
		this.historyService = historyService;
		this.explorer = explorer;

	}

	@Override
	protected void completeProcess() {

		if (!processEndNotified) {
			processEndNotified = true;
			super.completeProcess();
		}

	}

	// synchronized since in some case is it possible for several tasks to
	// complete at the same time, joining on the same next task. This can cause
	// a race condition where the next task is processed several times.
	@Override
	synchronized protected Collection<LearnPadTask> fetchNewTasks() {
		List<LearnPadTask> newTasks = new ArrayList<LearnPadTask>();

		// check for newly triggered tasks by getting the list of waiting
		// tasks...
		List<Task> waitingTasks = taskService.createTaskQuery()
				.processInstanceId(processId).list();

		final ProcessInstance processWithVars = runtimeService
				.createProcessInstanceQuery().includeProcessVariables()
				.processInstanceId(processId).singleResult();

		// ... ignoring already processed tasks
		for (Task t : waitingTasks) {
			if (!registeredWaitingTasks.contains(t.getId())) {

				Collection<LearnPadDocument> documents = new ArrayList<LearnPadDocument>();

				// add input data objects to task
				if (explorer != null) {
					Set<String> dataInputs = explorer.getDataInputs(t
							.getTaskDefinitionKey());

					for (String dataInput : dataInputs) {

						Collection<LearnPadDocumentField> fields = new ArrayList<LearnPadDocumentField>();

						for (String element : explorer
								.getDataObjectContent(dataInput)) {
							fields.add(new LearnPadDocumentField(element,
									element, "string", "", processWithVars
											.getProcessVariables().get(element)
									.toString()));
						}

						documents.add(new LearnPadDocument(dataInput, explorer
								.getDataObjectName(dataInput), "", fields));
					}
				}

				newTasks.add(new LearnPadTask(t.getProcessInstanceId(), t
						.getId(), t.getName(), t.getDescription(), documents,
						new Date().getTime()));

				registeredWaitingTasks.add(t.getId());
			}
		}

		return newTasks;
	};

	@Override
	protected void completeTask(final LearnPadTask task,
			final Map<String, Object> data) {
		// complete task and de-register it
		taskService.complete(task.id, data);
		registeredWaitingTasks.remove(task.id);
	}

	@Override
	protected boolean doesTaskExist(String taskId) {
		return taskService.createTaskQuery().includeProcessVariables()
				.taskId(taskId).singleResult() != null;
	}

	@Override
	protected boolean isTaskAlreadyCompleted(String taskId) {
		return historyService.createHistoricTaskInstanceQuery().finished()
				.taskId(taskId).singleResult() != null;
	}

	@Override
	protected Map<String, Object> getTaskInputs(String taskId) {
		return taskService.createTaskQuery().includeProcessVariables()
				.taskId(taskId).singleResult().getProcessVariables();
	}

	public void onEvent(final ActivitiEvent event) {

		if (event.getProcessInstanceId().equals(processId)
				&& event.getType().equals(ActivitiEventType.PROCESS_COMPLETED)) {
			completeProcess();
		} else {
			for (LearnPadTask newTask : fetchNewTasks()) {
				processNewTask(newTask);
			}
		}
	}

}
