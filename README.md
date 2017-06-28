* *Builds* [![Build Status](https://travis-ci.org/FMT-ISTI-CNR-UNICAM/BEBoP.svg?branch=master)](https://travis-ci.org/FMT-ISTI-CNR-UNICAM/BEBoP)

Verification understandability component plugin
====================

Information   | Value
------------- | --------
Component     | Verification Understandability Plugin
Partner       | Unicam, ISTI-CNR
WP            | 4
Responsible   | Andrea Polini <andrea.polini@unicam.it>
Collaborators | Giorgio Spagnolo [spagnolo at isti.cnr.it],  Damiano Falcioni <damiano.falcioni@unicam.it> 
Roadmap       | http://wiki.learnpad.eu/LearnPAdWiki/bin/view/Component/Model+Verification

# Summary
This component is a plugin for the verification component that provide understandability check over a LearnPAd model.

# How it works?
The plugin is specific for the Verification Component and provide understandability verification of a LearnPAd model. In order to work it must be placed in the plugin folder defined on the Verification Component and it will be automatically recognized.
This operation is currently automated by maven during the installation phase of the Verification Component.

# Configuration
No configuration needed

# Interfaces
In order to be recognized by the Verification Component, this plugin expose the following interface src/main/java/eu/learnpad/verification/plugin/interfaces/Plugin.java 
and define this MANIFEST.MF /src/main/resources/custom/MANIFEST.MF

The output structure of the verification provided by this plugin is reported in the following:


	<UnderstandabilityResult>			
   			<VerificationType>..type of the verification..</VerificationType>
   			<DefinitionID>PROCESS_1</DefinitionID> ..id of definition..
			<Status>OK or KO</Status> ..status of the verification..
			<Description>Summary Description of result</Description>
			<Guidelines>
				<Guideline id="" Name=""> ..id and name of the guideline..
					<Description>..detailed description of guideline..</Description>
					<Suggestion>..suggestion of the specific problem..</Suggestion>
					<Elements>..elements of the specific problem..
						<ElementID refProcessID="" refName="" >..bpmn object id..</ElementID>
					</Elements>
				</Guideline>
				...
				<Guideline id="" Name="">
					<Description>..detailed description of guideline..</Description>
					<Suggestion>..suggestion of the specific problem..</Suggestion>
					<Elements>..elements of the specific problem..
						<ElementID refProcessID="" refName="" >..bpmn object id..</ElementID>
					</Elements>
				</Guideline>	
				...		
			</Guidelines>
	</UnderstandabilityResult>

In case of any error in the verification phase, the plugin output will look like in the following:

	<ErrorResult>
			<Status>ERROR</Status>
			<Description>..error message..</Description>
	</ErrorResult>

Learn PAd project - Model-Based Social Learning for Public Administrations
==========================================================================

Learn PAd is a European financed project ([FP7](http://cordis.europa.eu/fp7/)).

# Presentation
In modern society public administrations (PAs) are undergoing a transformation
of their perceived role from controllers to proactive service providers, and are
under pressure to constantly improve their service quality while coping with
quickly changing context (changes in law and regulations, societal
globalization, fast technology evolution) and decreasing budgets. Civil servants
are challenged to understand and put in action latest procedures and rules
within tight time constraints. Learn PAd will build an innovative holistic
e-learning platform for PAs that enables process-driven learning and fosters
cooperation and knowledge-sharing. Learn PAd technical innovation is based on
four pillars:

1. a new concept of model-based e-learning (both process and knowledge)
2. open and collaborative e-learning content management
3. automatic, learner-specific and collaborative content quality assessment
4. automatic model-driven simulation-based learning and testing

Learn PAd considers learning and working strongly intertwined (learning while
doing).  The platform supports both an informative learning approach based on
enriched business process (BP) models, and a procedural learning approach based
on simulation and monitoring (learning by doing). Formal verification and
natural language processing techniques will ensure quality of content and
documentation. Specialized ontologies and KPIs will be defined to keep learners
engaged, while automatically derived tests will challenge their acquired
knowledge. Learn PAd is inspired by open-source communities principles and
cooperation spirit: contents are produced by the community, and meritocracy is
naturally promoted, with leaders emerging because of their skill and expertise.
Finally Learn PAd will contribute to dissemination and evolution of BPMN and
related modeling standards.

## Partners

|  #  | Name                               | Short name    | Country     | Project entry month | Project exit month |
| --- | ---------------------------------- | ------------- | ----------- | ------------------- | ------------------ |
|  1  | CONSIGLIO NAZIONALE DELLE RICERCHE | CNR           | Italy       | 1                   | 30                 |
|  2  | BOC ASSET MANAGEMENT GMBH          | BOC           | Austria     | 1                   | 30                 |
|  3  | Linagora Grand Sud Ouest SA        | LINAGORA GSO  | France      | 1                   | 30                 |
|  4  | NO MAGIC EUROPE UAB                | NME           | Lithuania   | 1                   | 30                 |
|  5  | REGIONE MARCHE                     | MARCHE REGION | Italy       | 1                   | 30                 |
|  6  | FACHHOCHSCHULE NORDWESTSCHWEIZ     | FHNW          | Switzerland | 1                   | 30                 |
|  7  | UNIVERSITA DEGLI STUDI DI CAMERINO | UNICAM        | Italy       | 1                   | 30                 |
|  8  | UNIVERSITA DEGLI STUDI DELL'AQUILA | UNIVAQ        | Italy       | 1                   | 30                 |
|  9  | XWIKI SAS                          | XWIKI SAS     | France      | 1                   | 30                 |


