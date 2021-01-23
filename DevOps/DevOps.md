# DevOps

Repo was at https://github.com/ssops with nl@ss.net but deprecate that and use 53rg3



## Todo

**Books**

- [ ] Site Reliability Workbook: Practical Ways to Implement SRE

- [ ] DevOPS Handbook: How to Create World-Class Agility, Reliability, and Security 

- [ ] DevOps For Dummies

- [ ] Continuous Delivery with Docker and Jenkins

- [ ] The DevOps Engineer’s Career Guide

- [ ] Python for DevOps: Learn Ruthlessly Effective Automation

- [ ] Python for Unix and Linux System Administration

- [ ] Microservices Security in Action

- [ ] Docker Cookbook - Second Edition

- [ ] Service Mesh books, see [here](https://www.amazon.com/s?k=service+mesh&ref=nb_sb_noss_2)

  

**Videos**

General

- [ ] [What is mutable vs. immutable infrastructure?](https://www.youtube.com/watch?v=II4PFe9BbmE)
- [ ] [What is a service mesh](https://www.youtube.com/watch?v=vh1YtWjfcyk)
- [ ] [Introduction to HashiCorp Consul](https://www.youtube.com/watch?v=mxeMdl0KvBI)
- [ ] [Introduction to HashiCorp Vault with Armon Dadgar](https://www.youtube.com/watch?v=VYfl-DpZ5wM)

Terraform

- [ ] [5 Lessons Learned From Writing Over 300,000 Lines of Infrastructure Code](https://www.youtube.com/watch?v=RTEgE2lcyk4)
- [ ] [Creating a Terraform Provider for Just About Anything](https://www.youtube.com/watch?v=noxwUVet5RE)
- [ ] [How to Build Reusable, Composable, Battle tested Terraform Modules](https://www.youtube.com/watch?v=LVgP63BkhKQ)

Git

- [ ] [Merge Conflict: Everything You Need to Know](https://dzone.com/articles/merge-conflict-everything-you-need-to-know)
- [ ] [What is Trunk-Based Development?](https://paulhammant.com/2013/04/05/what-is-trunk-based-development/)
- [ ] [Branching Models in a nutshell](https://medium.com/factualopinions/branching-models-in-a-nutshell-bf24ea1d888a)
- [ ] [Git to know this before you do Trunk Based Development](https://medium.com/factualopinions/git-to-know-this-before-you-do-trunk-based-development-tbd-476bc8a7c22f)



## Vocab

- **Continuous Integration / Continuous Delivery, CICD**

  CD: Automate the process of New Feature (development) => QA checks (tests) => Operations (deployment), i.e. create a Continuous Delivery pipeline. The automated deployment pipeline is a sequence of scripts that is executed after every code change committed to the repository. If the process is successful, it ends up with the deployment to the production environment. There are 3 phases:

  - Continuous Integration: 
    "It checks out the code from the repository, compiles it, runs unit tests, and verifies the code quality. If any step fails, the pipeline execution is stopped"

  - Automated Acceptance Testing: 
    Tests which verify that the software is what the client wanted. Most difficult part in the CD pipeline. Test preparations: Package app into JAR, crate Docker image from it, push to Docker Registry, trigger Docker host machine to pull the image, start it. Then Jenkins runs a suite of acceptance tests via Cucumber for example.

  - Configuration Management: 
    "It concerns taking care of preparing and installing the necessary tools, scaling the number of service instances and their distribution, infrastructure inventory, and all tasks related to the application deployment". 
  "Ansible takes care of the environments and enables the deployment of the same applications on multiple machines. As a result, we deploy the application to the staging environment, run the acceptance testing suite, and finally release the application to the production environment, usually in many instances (on multiple Docker Host machines)."
    
    

- **DevOps**
  Development & operations
  ![img](https://learning.oreilly.com/library/view/continuous-delivery-with/9781787125230/assets/afdeb6b7-d317-4bf8-bfd9-495e79910340.png)

  Stuff you have to deal with:

  - Automated build, test, package, and deploy operations

  - Quick pipeline execution: The pipeline must be executed in a timely manner, preferably in 5-15 minutes.

  - Quick failure recovery: A possibility of the quick rollback or system recovery is a must.

  - Zero-downtime deployment

  - Trunk-based development: See Git Work Flow.

    


- **Flyway migration**
  Framework for SQL database migrations. Available as CLI program, Java API (code), Maven & Gradle plugin.

- **Git Work Flow**

  - **Feature Branch Development** (GitFlow model), see [here](https://nvie.com/posts/a-successful-git-branching-model/)
    Create a new branch for every feature we take care of and maintain this branch until we can merge it with the mainline. In the meantime, we must check out to a hotfix branch, resolve merge conflicts, remember about our branches, etc. Can lead to merge hell.

  - :heavy_exclamation_mark: TODO Trunk Based Development, see todo Git

  
  
- **Testing in DevOps**

  - Acceptance Testing (automated) - determine if the requirements of a specification or contract are met

  - Unit Testing (automated) - testing single components of the software

  - Exploratory Testing (manual) - try to break the software manually

  - Non-functional Testing (automated) - test system properties related to the performance, scalability, security, etc.

    

- **Schema migration**
  Refers to the management of incremental, reversible changes and version control to relational database schemas. A schema migration is performed on a database whenever it is necessary to update or revert that database's schema to some newer or older version.

- **User Acceptance Testing (UTA)**
  Mostly manual. The QA team performs a suite of Integration Testing, Acceptance Testing, and Non-functional Testing (performance, recovery, security, and so on). Any bug that is detected goes back to the development team. After the UAT phase is completed, the QA team approves the features that are planned for the next release.

- **User Story**
  Informal, natural language description of one or more features of a software or feature, i.e. requirements.

  

  




## Common Tools

- **Docker**
  Let's you put applications into containers (lightweight VMs) with all the required libs, configurations, etc. So you can do fun stuff like having different versions of the same app on one single host. The container exposes a port which allows you to communicate with the app inside the container.
- **OpenStack**
  Seems like a complete tool set to run a VPS company. You can provision VPS on remote servers via images, configure networks between the VPS, manage storages, manage authentication between services, etc.
- **Ansible**
  Configure infrastructure as code. See PNA.
- **Terraform**
  Provisioning of infrastructure as code. Automate and manage your infrastructure. 
- - [ ] Jenkins
- - [ ] EFK Stack
- **Prometheus** 
  Metrics of server and apps. Like EFK you need modules which publish the data. Your app can expose a metrics endpoint.
- - [ ] HAProxy
- **Kubernetes**
  Container orchestration. Manages your cluster of Docker containers, e.g. zero-downtime procedure, horizontal scaling, backup and restore in case of disaster. 
- - [ ] Ispop? How to authenticate servers to each other?
- - [ ] Docker Swarm, like Kubernetes, but different...