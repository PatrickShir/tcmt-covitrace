# Covitrace with Microservice Pattern

### Coding standards and other rules
* Before working on any new features, create a new feature branch.
* Create unit tests and possibly integration tests to check if your code works as expected.
* When writing integration tests extend the Test class with ``AbstractIntegrationTest``.
* When it covers at least 100% class and methods, 80% lines of code and all tests passes,
    
    then merge the feature branch with the dev branch.
* After each sprint if all test still passes in the dev branch, merge with master branch.

<h3>Update:</h3> Developers can now manipulate and program directly from the root folder.

### Guide on how to run and stop the application

Navigate to the root folder. ``/covitrace``

* If changes have been made in any microservice then execute the command below in the terminal to generate new jars.

    ``gradle clean build``


 * To run the application itself a docker-compose command is necessary.

    * <u>If you are running the application for the first time or want to rebuild the containers then use this command:</u>

        ``docker-compose up --build``
    
        This will allow your docker-compose file to build and run all services and dependencies in docker containers.
    
    * <u>Else use this command:</u>

        ``docker-compose up``
    
    * If you want to run the docker application in the background, then use the
        ``docker-compose up --detach`` or ``docker-compose up --build --detach``. This will cause the docker app to run in detached mode, meaning that the terminal
      isn't bound to the process.
    

* If you want to stop the application press ``Ctrl + C`` in terminal that is responsible for the running containers. If
    
    you are running the docker app in a detached mode, then use the ``docker-compose stop`` command in the terminal.


### Other tips:

Run ```gradle -q projects``` in the terminal from the root folder to see the subprojects.

If you wish to add another subproject(microservice) navigate to the settings.gradle file in the root folder,
and from there add the name of the project to the ``include`` keyword. Example ``include ..., 'new-service'``.

<strong style="font-size: large; color: firebrick">Note</strong> that a microservice should only be added as a module.
Follow the other examples.

<strong>Pg-admin</strong> has been added to give you an overview of the postgres databases.

In your web browser when running the app, visit ``localhost:5555`` and login with the pgAdmin credentials that exist in the ``.env`` file.
Once you're logged in, click on "Add New Server". Add a name to the server (``covitrace``) and thereafter navigate
to the connection tab. In there add the postgres-hostname, -username and -password, all of which can be found in the ``.env`` file.

Finally, click "Save", and you'll have an overview of the existing databases.
