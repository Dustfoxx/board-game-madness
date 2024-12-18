# MindMGMT

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

The project can be run using `gradlew lwjgl3:run` from a command line within the `project` folder. A java distribution must exist on path.
The architecture follows the MVC model and consists of the folders in `core/src/main/java`. This is where you will find the bulk of the work.
`src`also contains a test folder with a few tests written for the control and model parts of the code.

Currently the game is playable through an entire game but some important features are missing.
- The recruiter should be invisible when agents are playing which is not the case right now.
- Should the game crash you have to completely restart the game if you're using vscode.
- No rules are shown in game so to understand the goal you'll have to check out the rulebook: https://offthepagegames.com/wp-content/uploads/2020/10/mm-RULEBOOK-v6.pdf.
- The game is supposed to be played on multiple computers. It will be done soon but is not in this current version.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.
- `html`: Web platform using GWT and WebGL. Supports only Java projects.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands. 
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `html:dist`: compiles GWT sources. The compiled application can be found at `html/build/dist`: you can use any HTTP server to deploy it.
- `html:superDev`: compiles GWT sources and runs the application in SuperDev mode. It will be available at [localhost:8080/html](http://localhost:8080/html). Use only during development.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.
