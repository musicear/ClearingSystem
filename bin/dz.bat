set BATCH_HOME=..\
@echo off
echo %BATCH_HOME%
set _LIBJARS=.;%BATCH_HOME%\lib;%BATCH_HOME%\lib\mq
for %%i in (%BATCH_HOME%\lib\*.jar) do call cpappend.bat %%i
for %%i in (%BATCH_HOME%\lib\mq\*.jar) do call cpappend.bat %%i
set _LIBJARS=%_LIBJARS%;../classes/
set BATCH_CP=%_LIBJARS%
echo %BATCH_CP%
set JAVA_OPTS=-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005
java %JAVA_OPTS% -Xmx200M -cp %BATCH_CP% junit.TestSendToMQ