# Docker

- Application containerization (see Containerization vs Virtualization). 

- Let's you put applications into containers (lightweight VMs) with all the required libs, configurations, etc. So you can do fun stuff like having different versions of the same program on one single host. So for example you can have PostgreSQL in 2 different versions, or 2 applications which use different Java versions. And you can update any required libraries for those without disturbing the other.

- The container exposes a port which allows you to communicate with the app inside the container. Containers can also share storage space.

- Containers run in isolation with their resources and don't affect each other, so a memory leak in app1 won't bring down app2. 

- More security. If an intruder gets access to one application, then he does get automatically get access to the rest of the system.

  

## Todo

- [Full Course](https://www.youtube.com/watch?v=3c-iBn73dDE&t=2522s) - basic commands, 
- Docker Swarm: This is a clustering and scheduling tool
- [ ] Are there performance tweaks for Docker?
- [ ] How to share storage between containers?
- [ ] How to update stuff in a container? E.g. you want an update to your, where does it go?
- [ ] Benchmark for Redis in Docker?
- [ ] How to make an app publish to Docker logs? Or generally... how to log? What about multiple logs?
  \> You need to use volumes which are published to the host, e.g. by another container for EFK.
- [ ] What about Docker security?
- [ ] How to utilize Docker for testing? E.g. automated test suite, setting up and destroying.



## Commands

- `docker diff <image_id>`
  Show file changes done in the image. Like Git.
- `docker container ...`
  Manage containers. For more see [full list](https://docs.docker.com/engine/reference/commandline/container_ls/).
- `docker container ls` - List all locally available containers. Use with `--all` for inactive ones. Shorter version is `docker ps`.
  - `docker container rm <cont_id>` - Remove container
- `docker container prune` - Remove all stopped containers
- `docker image ...`
  Manage images. For more see [full list](https://docs.docker.com/engine/reference/commandline/image/).
- `docker image ls` - Lists all locally available images
  - `docker image rm <image_id>` - remove image, you need to delete its container first. `docker rmi <image>` also works.
- `docker inspect <cont>`
  Shows you the configuration of a container. Networking details IP addresses, ENVs settings, etc. For debugging.
- `docker info`
  Basic infos about Docker Engine, e.g. how many containers are running.
- `docker logs <cont>`
  Show the logs. 
- `docker start` & `docker run`
  Run starts and CREATES a new container, it also downloads images if necessary. Starts only starts one. Use `-i` to start a container in interactive mode.




## Options

Options to run containers and others.

- `docker run -e`
  Set ENVs when running the container, e.g. `docker run -e MY_NAME="John Doe" your_image`. Cool when you have different contexts in an app. E.g. testing & prodution.
  
  - Can only be used when constructing the container with `run`. 
  - It  overwrites corresponding ENVs in your Dockerfile. 
  - It's also persistent, i.e. once set they stay in the container.
  
  ```
  docker run -e MY_NAME="John Doe" your_image
  ```
  
- `docker run -d` (--detach)
  To run a container in the background as daemon. Only works with `docker run`. Using `start` run generally detached.
  
  ```
  docker run -d tomcat
  ```
  
- `docker run -i <image>` or `docker start -i <cont>`
  Starts container in interactive mode. I.e. terminal will be attached to the terminal output of the starting script.

- `docker run -p <host_port>:<container_port>`
  Publishes a port from your app to some port on its host. Same as `-e`, only works with `run` and is persistent.

- `docker run -t`
  Gives you a terminal. See [here](https://stackoverflow.com/a/40026942/4179212).

- `docker run -d --name tomcat tomcat`
  Name the container on creation. Makes life easier. Otherwise you get some random name.

- `docker -H <server_ip>:2375 <your_commands>` 
  Run Docker commands on a remote server, e.g. this would run the `hello-world` image as a container:

  ```
  docker -H <server_ip>:2375 run hello-world
  ```

- `docker run -v <host_path>:<container_path>`
  Mounts the volume at `<host_path>`  from your host onto `<container_path>` in the container.  E.g.

  ```
  docker run -it -v /var/www:/var/www ubuntu:16.04
  ```

  You might want to look into `docker volume create`, see [here](https://docs.docker.com/engine/reference/commandline/volume_create/). Gets you unified naming. But doesn't seem to be that useful?



## Dockerfile

See [docs](https://docs.docker.com/engine/reference/builder/) for more (see right menu).

**Build an image**

Note: The `.` instructs Docker to take the file `Dockerfile` in the local directory. Additionally you can add a tag to the name `name:tag`

```
docker build -t <desired_image_name> .
```

**Nice2Knows**

- When you issue a docker build command, the current working directory is called the build context. By default, the Dockerfile is assumed to be located there. For different directory see [here](https://docs.docker.com/develop/develop-images/dockerfile_best-practices/#understand-build-context).

**Instructions**

- `FROM <some_image>` 
  Your parent image to build ontop.
  
- `RUN <some_command>` 
  Execute any kind of terminal commands. See [docs](https://docs.docker.com/engine/reference/builder/#run) for command with arguments.
  
- `ENTRYPOINT ["executable", "param1", "param2"]` 
  Execute this command when the container starts. `executable` can be anything. E.g. `ENTRYPOINT ["python3", "hello.py"]` would run the Python interpreter with the file hello.py
  
- `COPY [--chown=<user>:<group>] <src>... <dest>`
  
  - You can use wildcards (e.g. for prefixes): `COPY hom* /mydir/`
  
- `ADD [--chown=<user>:<group>] <src>... <dest>` 
  
  - Can also handle URLs
  
- `ENV MY_NAME="John Doe"`
  Set environment variable "MY_NAME" to "John Doe". if you don't need that env in the final build you can also use `ARG` (see [here](https://docs.docker.com/engine/reference/builder/#arg)). You can also set them when running the container / image, e.g. 
  `docker run -e MY_NAME="John Doe" your_image`
  
- `EXPOSE <port>`
  Expose a port. Use to make your app inside the image connectible to the host. This does not publish the port (see option `-p`)
  
- `VOLUME /host_directory`
  /host_directory will be mapped into the host's default directory for volumes, /var/lib/docker/vfs/. This is a good solution if you deliver an application as an image and you know it needs the permanent storage for some reason (for example, storing application logs).
  
  


## Nice2Knows

- On Docker Hub, look into tags to see when the last commit was, before pulling it. E.g. search results say `jenkins` is the "official Docker image", but [docs](https://www.jenkins.io/blog/2018/12/10/the-official-Docker-image/) say it's `jenkins/jenkins`. You can also click on the tag names to see which commands the Dockerfile uses.
- When you forgot a command just use TAB to see options. E.g. type `docker image` and press TAB to see all available commands.
- You can rename containers with `docker container rename CONTAINER NEW_NAME`.
- Docker Hub has a free tier for public images. You have also the option to host your own registry.
- With `docker save` you can export images to and with `docker load`  you can import them from a local file, see [here](https://stackoverflow.com/a/41099881/4179212).
- You can also use `docker save` to inspect an image, e.g. manifest.json & other container properties. See [here](https://stackoverflow.com/a/54821350/4179212).
- If you need only a simple container for an app, e.g. a Java application, then you can just use the respective image from the provider as parent image. E.g. `FROM openjdk:11.0.9-jdk`
- To detach an interactive container without stopping it you can press `ctrl+p` followed by `ctrl+q`.
- `docker run -it <image>` looks way cooler.
- To build containers you might want to look into [builder patterns](https://blog.alexellis.io/mutli-stage-docker-builds/).
- Docker can be great for testing, e.g. "check our Python code between different versions of Python, or on different Linux distributions such as Fedora, Ubuntu, CentOS, and so on."
- You can configure a logging driver, e.g. fluentd (as in EFK). See [here](https://docs.docker.com/config/containers/logging/).
- When specificing directories on the host use `$HOME` instead of the absolute path. Absolute path is fine when you develop locally.
- When using volumes on the host for persisting data you can just destroy containers and not care about.



## Best practices & anti-patterns 

- [Docker Best Practices and Anti-Patterns](https://medium.com/better-programming/docker-best-practices-and-anti-patterns-e7cbccba4f19)
- [Docker anti-patterns](https://codefresh.io/containers/docker-anti-patterns/)
- [Best Practices for Load Balancing Docker Containers](https://blog.snapt.net/best-practices-for-load-balancing-docker-containers)



## Debugging

- See "`docker inspect <cont>`"
- See "Inject a process (e.g. a shell) into a container"
- See "`docker logs`"
- You can start the Docker Engine `dockerd` in debug mode, but that only gets you DEBUG level logs additionally. You can view the logs via `journalctl -u docker.service` but the DEBUG level stuff isn't there.
- `docker events` shows events like start and stop of containers. It's a live view, so you need to trigger some event to see it. You can see the history (since `dockerd` started) via `docker events --since '2015-01-01'`.



## Performance

- You can use [cAdvisor](https://github.com/google/cadvisor) or [Prometheus](https://prometheus.io/) for performance monitoring. For cAdvisor you can just run the Quick Start example which sets it up with a web UI. Click on "Docker Containers" at the top to see stats for individual containers. It's like htop, but with additional info like network thoughput. 
- When using applications with "high packet rates", e.g. Redis. See [here](https://stackoverflow.com/questions/26558001/running-redis-with-docker-performance-issue). Network performance can be improved by using `--net=host`. Should only be used for trusted containers: "The host option makes the container network interfaces identical to the host. They share the same IP addresses, so everything started on the container is visible outside."



## Security

- [Docs](https://docs.docker.com/engine/security/)

- [Docker Container Security 101: Risks and 33 Best Practices](https://www.stackrox.com/post/2019/09/docker-security-101/)
- [10 Docker Security Best Practices](https://phoenixnap.com/kb/docker-security-best-practices)
- [Best Practices for Docker Security For 2020](https://www.securecoding.com/blog/best-practices-for-docker-security-for-2020/)



## Encountered Problems

- **Cannot connect to the Docker daemon at unix:///var/run/docker.sock. Is the docker daemon running?** 
  See solutions [here](https://appuals.com/cannot-connect-to-the-docker-daemon-at-unix-var-run-docker-sock/). Only thing that worked was `sudo dockerd &`. Rebooting is also an option.



## Docker Compose

See [docs](https://docs.docker.com/compose/). Tool to define multicontainer Docker applications, i.e. multiple containers on one machine. You define a multicontainer application in a single file and feed it to Docker Compose, which sets up the application. Awesome to define your app in one place, instead of having it inside of Bash code. 

- Must be installed `sudo pip install docker-compose`

- Example `docker-compose.yml` file. Basic just a `docker run` in pretty.

  ```yaml
  version: "3.1"
  services:
      wordpress:
          build: .		# build from Dockerfile in your current dir
          restart: always
          ports:
              - 8080:80
          environment:
              WORDPRESS_DB_PASSWORD: example
      mysql:
          image: mysql:5.7
          restart: always
          environment:
              MYSQL_ROOT_PASSWORD: example
  ```

- Run Docker Compose `docker-compose up`

**Nice2Knows**

- Development environments
  "The Compose file provides a way to document and configure all of the application’s service dependencies (databases, queues, caches, web service APIs, etc)" and start it with a single command `docker-compose up`.
- Docker Compose YAML file reference, see [here](https://docs.docker.com/compose/compose-file/)
- Command line reference, see [here](https://docs.docker.com/compose/reference/overview/)



## Docker Swarm

See [docs](https://docs.docker.com/engine/swarm/). Orechstration tool. Use "Docker Swarm Mode". "Docker Swarm" is the old version. And SwarmKit is a toolkit used by Docker Swarm Mode. ""Swarm" refers to traditional Swarm functionality, "Swarm Mode" refers to new Swarm mode added in 1.12, "Swarmkit" refers to the plumbing open source orchestration project" (see [here](https://dockerlabs.collabnix.com/intermediate/swarm/difference-between-docker-swarm-vs-swarm-mode-vs-swarmkit.html)). Docker Swarm mode supports two types of nodes; a manager and a worker. Manager nodes perform the orchestration and cluster management functions for the Swarm. They dispatch units of work called tasks to the workers. The manager nodes use the [Raft Consensus](http://thesecretlivesofdata.com/raft/) algorithm to manage the global cluster state. The worker nodes receive and execute the tasks that are dispatched by the managers.

For a tutorial with Virtualbox from Docker Cookbook - Second Edition see `_res/Docker/`.

**Nice2Knows**

- For Raft to work correctly, you need an odd number of managers in order for the leader election to work properly. That means that, if you want a fault-tolerant Swarm cluster, you should have either three or five managers.



## Cookbook

### Installing Docker on Linux with an automated script

```bash
curl -fsSL get.docker.com -o get-docker.sh
sudo sh get-docker.sh        
```

### Install Docker on a remote server

- By default, due to security reasons, Docker runs via a non-networked Unix socket that only allows local communication. It's necessary to add listening on the chosen network interface socket so that the external clients can connect. See [configure and troubleshoot the Docker daemon](https://docs.docker.com/config/daemon/), also check [control Docker with systemd](https://docs.docker.com/config/daemon/systemd/).
- The second step of server configuration concerns the Docker security certificates. This enables only clients authenticated by a certificate to access the server. See [protect the Docker daemon socket](https://docs.docker.com/engine/security/https/). This step isn't strictly required; however, unless your Docker daemon server is inside the firewalled network, it is essential. Note: Docker has a REST API. So without security everyone can control it.
- If your Docker daemon is run inside the corporate network, you have to configure the HTTP proxy.

### Adding a nonroot user to administer Docker

"We can enable other users to use Docker by adding them to the docker group" FOR CONVENIENCE ON YOUR LOCAL MACHINE. You need to log out in Linux or perform some sort of refresh for this work. The change is somehow not recognized immediately.

```bash
sudo usermod -aG docker <username>
```

### Setting the restart policy on a container

There are also other options. See [docs](https://docs.docker.com/config/containers/start-containers-automatically/).

```bash
docker container run --restart=always -d -i -t ubuntu /bin/bash
```

### Inject a process (e.g. a shell) into a container

For a running container you need to use `exec`.

```bash
docker container exec -it <container_id> /bin/bash
```



## Resources & Links

- [Commands](https://docs.docker.com/engine/reference/run/) (see menu)
  
- [Install Docker](https://docs.docker.com/engine/install/ubuntu/)
  
- [Docker Hub Explore](https://hub.docker.com/search?q=&type=image)
  
- [Run your app in production](https://docs.docker.com/get-started/orchestration/) + Lots of tutorials, see menu



## Vocab

- **Base Image** (actually called parent image)
  See [here](https://stackoverflow.com/q/20274162/4179212). Each Docker image your create is a stack layers. The parent image is used to apply changes on. OS images come with CLI tools to build your desired image layers. E.g. Ubuntu uses `apt`, CentOS uses `yum`. 

- **Containerization vs Virtualization**
  Containerization is like virtualization but without the bloat of a complete operating system, i.e. much less overhead.

  ![image-20210108150507257](/home/cc/Desktop/Programming/notes/DevOps/_res/Docker/image-20210108150507257.png)

- **Dockerized Application**
  A dockerized application (web service) is run as a container on a Docker Host and is reachable as it would run directly on the host machine. 

- **Docker Compose**
  Tool to define multicontainer Docker applications, i.e. multiple containers on one machine. You define a multicontainer application in a single file and feed it to Docker Compose, which sets up the application.

- **Dockerfile**
  Specifications as text for creating images.

- **Docker Engine**
  "Docker Engine is a client-server application that creates and manages Docker objects, such as images and containers". It consists of three components: Docker Daemon (server) running in the background,
Docker Client running as a command tool and a REST API (like Docker Client, but for external control).
  
- **Docker Registry**
  Storage for dockerized applications

- **Docker Swarm**
  Cluster multiple Docker hosts, like Kubernetes.

- **Image & Container**
  An image is a stateless building block in the Docker world. You can imagine an image as a collection of all files necessary to run your application together with the recipe on how to run it. Because they're stateless, they can be stored as a file. You can also build an image on top of another image.
  A container is a running instance of an image. It is stateful and we can change its states. You can run multiple containers of the same image on a single host.

