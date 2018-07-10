# LinTra

LinTra is a model transformation engine that by means of parallelizing tasks and using a data-based approach, is able to execute model transformations efficiently. LinTra offers support for the execution of outplace as well as inplace model transformations running both in batch mode—the input model is available once the transformation starts—and streaming mode—the input model is a data stream.

In our last release, we have integrated in the current implementation of LinTra libraries which provide it with uncertain types and operations as primitive types. In particular, these libraries offer support for the types UReal, UInteger and UBoolean [2].

Since LinTra is a Java-based platform, its metamodels and transformations are written in Java too.

# Case Studies

This file gives instructions about how to run the software artifacts. For more information about the approach, we refer the reader to our paper [1].

## Requirements/Dependencies

- Eclipse IDE (tested with Eclipse Oxygen.2).
- Java 8

## Running the Surveillance Case Study

In order to run the case study, the reader has to follow the following steps:

1. Import Java projects into a workspace
2. Move to the project: lintra.examples.drones
3. Move to the package: transformations.confidence.surveillance
4. Run the Java file: SurveillanceRunner.java. Note that the Java Heap Space might need to be increased with the parameter -Xmx10G.
5. Move to the package: transformations.plain.surveillance
6. Run the Java file: PlainSurveillanceRunner.java. Note that the Java Heap Space might need to be increased with the parameter -Xmx10G.

The execution times reported in our paper [1] are shown in console after executing steps 4 (with confidence) and 6 (without confidence).

## 2. Social Media

In order to run the case study, the reader has to follow the following steps:

1. Import Java projects into a workspace
2. Move to the project: lintra.examples.socialmedia
3. Move to the package: transformations.confidence.socialmedia
4. Run the Java file: ConfidenceSocialMediaRunner.java. Note that the Java Heap Space might need to be increased with the parameter -Xmx10G.
5. Move to the package: transformations.plain.socialmedia
6. Run the Java file: PlainSocialMediaRunner.java. Note that the Java Heap Space might need to be increased with the parameter -Xmx10G.

The execution times reported in our paper [1] are shown in console after executing steps 4 (with confidence) and 6 (without confidence).

## 3. Smart Home

In order to run the case study, the reader has to follow the following steps:

1. Import Java projects into a workspace
2. Move to the project: lintra.examples.smarthome
3. Move to the package: transformations.confidence.smarthome
4. Run the Java file: ConfidenceSmartHomeRunner.java. Note that the Java Heap Space might need to be increased with the parameter -Xmx10G.
5. Move to the package: transformations.plain.smarthome
6. Run the Java file: PlainSmartHomeRunner.java. Note that the Java Heap Space might need to be increased with the parameter -Xmx10G.

The execution times reported in our paper [1] are shown in console after executing steps 4 (with confidence) and 6 (without confidence).

# References

[1] Loli Burgueño, Manuel F. Bertoa, Nathalie Moreno, Antonio Vallecillo. Expressing Confidence in Models and in Model Transformations Elements. In Proc. of the ACM/IEEE 21th International Conference on Model Driven Engineering Languages and Systems 2018 (MODELS 2018). Copenhagen, Denmark, October 2015.

[2] Nathalie Moreno, Manuel F. Bertoa, Gala Barquero, Loli Burgueño, Javier Troya, Antonio Vallecillo. Expressing Uncertainty in OCL/UML Datatypes. In Proc. of the 14th European Conference on Modelling Foundations and Applications (ECMFA'18), pp. 46-62.
