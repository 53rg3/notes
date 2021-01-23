# Jenkins

- Automation server. Continuous Integration and Continuous Delivery pipelines.

- Can run any other automated sequence of scripts. 

- Highly plugin-oriented.

- Usually, the whole team, or even the whole organization, uses the same Jenkins instance. Therefore you have a master/slave architecture. The Jenkins master accepts a build request, but the execution is started at one of the Jenkins Slave (agent) machines. Such an approach provides horizontal scaling of the Jenkins environment.

- Jenkins is critical to the whole company. That is why it needs to be highly available, so it is definitely not for the purpose of testing. It means there should always be two instances of the same Jenkins infrastructure: test and production. Test environment should always be as similar as possible to the production.

- Every commit to GitHub automatically triggers the Jenkins build (continuous integration), which uses Gradle to compile Java code, run unit tests, and perform additional checks (code coverage, static code analysis, and so on).

- Netflix has test and production master instances, each of them owning a poll of slaves and additional ad hoc slaves. Altogether, it serves around 2000 builds per day.

  ![image-20210113083212954](/home/cc/Desktop/Programming/notes/DevOps/_res/Jenkins/image-20210113083212954.png)



## Todo

:green_heart: Continue here: https://learning.oreilly.com/library/view/continuous-delivery-with/9781787125230/1e59c33e-855e-4b32-bdd0-6f1d7b68fff2.xhtml

- [ ] How to store random scripts on Jenkins? Can we create an "admin UI" with? I.e. run script via single click on specific server?
- [ ] [TechWorld Jenkins tutorial](https://www.youtube.com/watch?v=tuxO7ZXplRE&list=PLy7NrYWoggjw_LIiDK1LXdNN82uYuuuiC&index=2), with multi-branch pipeline
- [ ] How to build master/slave architecture?
- [ ] :heavy_exclamation_mark:Check for better books about building Jenkins agents
- [ ] Best practices, see [TOC, last chapter](https://learning.oreilly.com/library/view/continuous-delivery-with/9781787125230/#toc)



## Configuring Agents

- Master and slaves communicate via SSH. JavaWS has been deprecated since JDK9.

- **Permanent agents:**

  - Manually set agent. Must be managed manually.
  - Dashboard > Manage Jenkins > Manage Nodes & Clouds > New Node

- **Permanent Docker agents**

  - General-purpose slaves. Each slave is identically configured (Docker Engine installed) and uses Docker images to build stuff.

- **Jenkins Swarm agents**

  - Allows you to dynamically add slaves without the need to configure them in the Jenkins master.

  - Install the Self-Organizing Swarm Plug-in Modules plugin in Jenkins. And check some [tutorial](https://duckduckgo.com/?q=Jenkins+Swarm+tutorial&ia=web). Basically you execute a command on a JAR on the agent server to connect to the master.

    ```bash
    java -jar swarm-client.jar -master <jenkins_master_url> -username <jenkins_master_user> -password <jenkins_master_password> -name jenkins-swarm-slave-1
    ```

- **Dynamically provisioned Docker agents**
  
  - Dynamically create a new agent each time a build is started. Number of slaves dynamically adjusts to the number of builds.
  - Dashboard > Manage Jenkins > Manage Nodes & Clouds > Configure Clouds
  - You can have a external or internal (e.g. `tcp://127.0.0.1:2376`, see tool tip) Docker hosts.
  - Check tutorials like [this](https://devopscube.com/docker-containers-as-build-slaves-jenkins/) for more.
  - The solution is somehow similar to the permanent Docker agents solution, because in result, we run the build inside a Docker container. The difference is, however, in the slave node configuration. Here, the whole slave is dockerized, not only the build environment. Advantages:
    - Automatic agent lifecycle: The process of creating, adding, and removing the agent is automated.
    - Scalability: Actually, the slave Docker host could be not a single machine, but a cluster composed of multiple machines. See [O'Reilly book](https://learning.oreilly.com/library/view/continuous-delivery-with/9781787125230/c107dd88-d2db-4f98-ab81-5db9ac7f5d92.xhtml).

### Settings

- **# of executors**: This is the number of concurrent builds that can be run on the slave
- **Remote root directory:** Use the same Jenkins workspace everywhere: `/var/jenkins`
- **Labels**: This includes the tags to match only the specific builds (tagged the same), for example, only projects based on Java 8
- **Usage**: This is the option to decide whether the agent should be used only for matched labels (for example, only for Acceptance Testing builds) or for any builds
- **Launch method**:
  - Launch agent via execution of command on the master
    Custom command run on the master to start the slave. E.g. `ssh <slave_hostname> java -jar ~/bin/slave.jar`
  - Launch agent by connecting it to the master
    "Known as “Launch agent via Java Web Start” before 2.176.1". Tool tip says: "In this case, a JNLP file must be opened on the agent machine, which will establish a TCP connection to the Jenkins master."
  - Launch agents via SSH
    Standard SSH connection. Might be the easiest option?

- **Node Properties:** Set environment variables & tool locations (if not default)

### Testing Agents

Add sleep to make a build take longer.

```groovy
pipeline {
     agent any
     stages {
          stage("Hello") {
               steps {
                    sleep 300 // 5 minutes
                    echo 'Hello World'
               }
          }
     }
}
```



## Pipelines

See [docs - Pipeline Syntax](https://www.jenkins.io/doc/book/pipeline/syntax/) for details.

"A pipeline is a sequence of automated operations that usually represents a part of software delivery and the quality assurance process. 

- It can be simply seen as a chain of scripts". 
- Operations are grouped together into stages (also known as gates or quality gates). If one stage fails, no further stages are executed. Each stage can consist of multiple steps (e.g. checkout code from repository, execute a script). 
- A declarative pipeline is always specified inside the pipeline block and contains 
  - sections - stages, steps, post (i.e. post scriptum)
  - directives - key-value pairs for configuration of  the pipeline, see [docs (right menu)](https://www.jenkins.io/doc/book/pipeline/syntax/) for details.
  - steps - single commands (e.g. `sh`, `custom`, `script`, see [here](https://www.jenkins.io/doc/pipeline/steps/) for more)

### Example

```groovy
pipeline {
     agent any
     triggers { cron('* * * * *') }
     options { timeout(time: 5) }
     parameters { 
          booleanParam(name: 'DEBUG_BUILD', defaultValue: true, 
          description: 'Is it the debug build?') 
     }
     stages {
          stage('Example') {
               environment { NAME = 'Rafal' }
               when { expression { return params.DEBUG_BUILD } } 
               steps {
                    echo "Hello from $NAME"
                    script {
                         def browsers = ['chrome', 'firefox']
                         for (int i = 0; i < browsers.size(); ++i) {
                              echo "Testing the ${browsers[i]} browser."
                         }
                    }
               }
          }
     }
     post { always { echo 'I will always say Hello again!' } }
}
```

See [docs - Pipeline Syntax](https://www.jenkins.io/doc/book/pipeline/syntax/) for details.

1. Use any available agent.

2. Execute automatically every minute.

3. Stop if the execution takes more than 5 minutes.

4. Ask for the Boolean input parameter before starting.

5. Set Rafal as the environment variable NAME.

6. Only in the case of the `true` input parameter (`params.DEBUG_BUILD`):

   - Print Hello from Rafal
   - Print Testing the chrome browser
   - Print Testing the firefox browser

7. Print I will always say Hello again! no matter if there are any errors during the execution.
   `post` - instructions that are run at the end of the pipeline build; marked with a condition (for example, always, success, or failure)

   

## Nice2Knows

- You utilize Docker for agents to make the builds, i.e. an agent acts as a Docker host and a pipeline is built inside a Docker container. So you can for example compile with different Java versions. 
- When the agents are set up correctly, it's possible to switch the master node offline, so that no builds would be executed on it and it would serve only as the Jenkins UI and the builds' coordinator. I.e. master is then only a manager and can't be used as agent. 
  - "master node offline or setting # of executors to 0 in the Manage Nodes configuration"
- In Manage Jenkins > Configure System you can enter a global system message, which will be shown to all users. But no HTML allowed so it's easy overlook.
- When you upgrade Jenkins then you can just reuse the working directory and all configuration will be kept in place. But plugins might show dependency issues if you use incompatible version, e.g. downgrade from jenkins:latest to jenkins:lts.
- Jenkins is configured using XML files and it provides the Groovy-based DSL language to manipulate over them. That is why we can add the Groovy script to the Dockerfile in order to manipulate the Jenkins configuration. What is more, there are special scripts to help with the Jenkins configuration if it requires something more than XML changes, for instance, plugin installation. 
- It makes sense to create your own preconfigured Docker image for the master. See [docs](https://github.com/jenkinsci/docker) for options.



## Cookbook

### Install

Change `YOUR_HOST_PORT` to your desired port and `/path/to/HOST/jenkins_home` to the absolute path where you want Jenkins to store its data. When you're on a server then better use `$HOME` instead of the absolute path.

```bash
docker run \
-p <host_port>:8080 \
-v <dir_on_host>:/var/jenkins_home \
--name jenkins jenkins/jenkins
```



## Vocab

- **Agent**
  Slave.
- **Master/Slave architecture**
  Master is the manager which takes build tasks and sends them off to its slaves (agents).