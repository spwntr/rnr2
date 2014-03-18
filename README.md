README

Project note 1/21/2014:  InfoSec has told us that if we are to continue improving RnR and connect it to the SalesForce API,
we will need to make the project 100% JavaScript due to Java's security vulnerabilities.  We also need to include them
(infosec@thoughtworks.com) in any discussions about integration with the SalesForce API and changing the project
to JavaScript.

-GD

~~~~~~~~~
Thanks for your interest in contributing to the ThoughtWorks RnR project!

To get started, you will want to clone and set this project up locally, helpful tips are included below.

To start contributing to this project, you will need to contact Alyssa Nabors (anabors@thoughtworks.com) for access to the Trello board,
where information project setup and current iteration is stored. You will also need to contact Landon Medlock (lmedlock@thoughtworks.com)
with your GitHub username to be added as a contributor to the project.
~~~~~~~~~

**Trello Etiquette**

We are using Trello (https://trello.com/) as our story tracker as well as for iteration planning and tracking purposes. Information about project contributors,
accessing the production server and ci pipeline, etc, can be found on the InfoBoard. Stories can be found on the currentIterationStoryBoard.

If you would like to work on a story, please use Trello to Assign yourself to the story and put it in "Doing".

If for any reason you ned to stop working on that story, please add an update on what you have accomplished in the comments, unassign yourself,
and move the story back to "Ready for Development".

This process should be used for stories being development as well as stories in QA.

Since it is not always possible for everyone on this project to attend a stand-up, it is important that you keep the Trello board up to date with
as much detail as possible.

~~~~~~~~~~~~~

**Things you might want to install**

    IntelliJ Ultimate Edition
    HomeBrew (http://brew.sh/ -- makes installation easy)
        Using Homebrew:
            Git
            Maven
            Tomcat


**********************************
BEFORE OPENING PROJECT IN INTELLIJ
**********************************

Run Maven compiler: compile in the directory where your pom xml file is located. (mvn compiler:compile)

To run the RnR on localhost:8080
	open Run -> Edit Configurations -> Server tab
		Plus sign (top left corner)
			select Tomcat server -> local

		Plus sign (bottom box)
			select Build Artifacts, select the exploded web app you created (only one option usually)

		Application Server: Tomcat 7.0 (copy/paste the CATALINA_BASE directory you get from typing catalina start in the terminal (remember to type catalina stop afterwards))

	Run -> Edit Configurations -> Deployment tab
Plus sign (for Deploy at the server startup box), select artifact, select the exploded web app artifact

~~~

**Troubleshooting**

FEEL FREE TO ADD ANY TIPS OR PROBLEMS YOU ENCOUNTER TO THIS LIST


If code shows errors due to missing libraries:
	import libraries: junit-4.1.0 and spring-framework-web if it's not there

If 'catalina start' in the terminal does not work:
	Install Tomcat 7.0.42
	have port 8080 open

If there is no artifact:
	Project Structure - Artifacts tab
	Plus sign, added Web App Exploded, selected main folder
	Make sure to add all the libraries to the artifact

If Intellij is throwing errors after these steps:
    Delete the project
    Check out from VCS on the home IntelliJ page using GIT
    Then open the project from the home IntelliJ page
    Repeat all above steps as necessary

If you seem to be missing an inordinate number of libraries and the artifact cannot be deployed
    Check your version of Java.
    Less than 1.7 will cause really funky errors

~~~