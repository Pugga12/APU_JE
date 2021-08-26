# APU_JE
A in-development discord bot
## Branch Status
Code in this branch is currently STABLE
# Building/Running
## Prerequisites
It is recommended that you download the build from CI. If you would like to build the jar yourself, run the buildDependents task and continue to Step 2.\
Requirements: At least Java 16 if building with gradle, or any modern browser when downloading from CI
## Downloading from CI
1. Go to https://ci.appveyor.com/project/Pugga12/apu-je/build/artifacts and download build\libs.zip\
If downloading the javadoc for this release, download build\docs\javadoc.zip
2. Extract the zip file to a empty folder. Copy the contents of https://github.com/Pugga12/APU_JE/blob/stable/example.env into a file called .env
and place your token in the token key
3. Run the jar. You must run the jar that says something like APU-X.X.X_XX-all.jar 
You're done. The jar will generate the files required and run the bot
