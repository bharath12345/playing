{{{
    "title": "Running Machine-Learning Assignments on my Laptop's Spark Cluster",
    "subheading": "",
    "tags" : [  ],
    "category" : "technology",
    "date" : "12-05-2014",
    "description" : "",
    "toc": true
}}}

The latest offering of Coursera's popular course on Machine Learning by Andrew Ng started in the first week of March. The course requires Matlab's Octave to be used to solve the assignments. Apart from trying to solve the problems in Octave, I decided to solve the assignments in the programming language of my choice - Scala. This is a quick post on the why's and the how's.

### Why Scala, Spark and Distributed?
As computing and web has grown, the size of data to process has grown much larger. To process these large volumes of data requires two things - (1) horizontal scalability/distribution (2) efficient usage of multicore compute. Languages like R and Octave are not built for either - that is, writing programs that run on a cluster and efficiently use all CPU/RAM is infeasible in these. They are good only for smaller datasets and POC (proof-of-concepts) on large production-like datasets. Large datasets and continuous data-streams requires software design and programming in compiled languages like C, JVM-based, Haskell etc. My preference is the JVM based languages. In the world of JVM, there are multiple open source frameworks that provide a platform to write statistical computing algorithms that run on a cluster, for example -

* [Apache Spark](https://spark.apache.org)
* [Apache Mahout](https://mahout.apache.org/)
* [H20](http://0xdata.com/)

I chose Spark. Spark is written in Scala. Its MLib implementation includes many of the popular/simpler ML algorithms. Spark makes use of Mesos or HDFS for distribution support. It started as research project at AMPLabs at UC Berkeley and is now incubated at Apache.

### Spinning up a Spark Cluster with Vagrant and Docker
Running a cluster on laptop firstly requires it be computationally well powered. I use a 4-CPU & 8GB-RAM Mac OSX machine. I would suggest that this is the minimum configuration.

The second requirement is to have separation of the virtual machines that form the cluster from the system that runs it. I have found Vagrant to be a superb tool to run configurable virtual machines which can be shared with ease. [Vagrant](https://vagrantcloud.com/) uses VirtualBox. I created a VM with Ubuntu 14 Trusty and allocated 2-CPU and 4GB-RAM for it exclusively on my laptop. The next idea is to run multiple VM's on this Ubuntu machine using [Docker](https://www.docker.io/).

Now why Docker? - If Vagrant provides heavyweight VM abstraction then Docker provides lightweight ones. The idea is to run multiple Docker based lightweight linux VM's on this Vagrant Ubuntu VM - this is because a Spark cluster needs multiple nodes like a Master, workers and Namenode (for HDFS). One can run Docker directly on the native machine using something like a TinyCore Linux OS. The steps to do so can be found on Docker's website. However it is better to avoid that and instead rely on Vagrant. There are couple of reasons for this -

* Tiny Core Linux's contents are not persisted across reboots. Since we would be coding on these VM's, a loss of contents is scary
* Allocating CPU/RAM to multiple nodes directly running in a laptop is unclean. Its not easy to achieve this CPU/RAM distribution in Docker too (along with shared folder support). 

Vagrant really comes handy to alleviate these shortcomings. Further, it is super easy to suspend a Vagrant VM and the whole cluster status will be persisted as-is... I can't think of anything more *cool* than that on the planet! 


### The Steps

1. My Vagrantfile is a small one. It uses Ubuntu 14.04 Trusty and allocates 4GB RAM and 2 CPU core exclusively -

    ```
    VAGRANTFILE_API_VERSION = "2"
    Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
      config.vm.box = "shrink0r/ubuntu-trusty-server-x64"
      config.vm.provider "virtualbox" do |v|
        v.memory = 4096
        v.cpus = 2
      end
    end
    ```

2. SSH into the Vagrant box and clone [this repository](https://github.com/amplab/docker-scripts) from AmpLabs to get going with the next Docker step
3. Post cloning, run the command - <code>sudo ./deploy/deploy.sh -i amplab/spark:0.9.0 -w 1</code> to bring up the cluster with 1 worker. We don't need more than 1 worker on our simulated cluster. And even with only one worker, there would be 4 nodes in this cluster (master, worker, namenode, domain name server). Expect this command to take quite some time to complete
4. The next necessary step is to configure name resolution. The nameserver IP to put in /etc/resolv.conf would be shown at the end out console output of command run in step-3
5. Follow the steps on the Github page of AmpLabs docker-scripts repo to make sure that a Scala shell can be attached and the example run
6. The next step is to download Hadoop and place it in the Vagrant system. Hadoop is required to interact with the HDFS (we need a client). I used Hadoop v1.2.1
7. The first assignment of Machine Learning course uses a txt file (ex1data1.txt) as data. The idea now is to place this on HDFS and run Spark linear regression on it. The HDFS in the AmpLabs cluster is created by a user called 'hdfs'. So we need to mimic a user with the same name on the Vagrant client system (this is a hack). So create a new user... my interaction -

    ```
    $ sudo useradd -m hdfs
    $ sudo passwd hdfs
    Enter new UNIX password: 
    Retype new UNIX password: 
    passwd: password updated successfully
    $ su hdfs
    Password: 
    $ whoami
    hdfs
    $
    ```

8. Next transfer the ex1data1.txt from the local filesystem to HDFS. Use the *hadoop* program from the downloaded Hadoop bundle (its in *bin*) to talk to the HDFS -

    ```
    hdfs@packer-virtualbox-iso:/vagrant$ hadoop-1.2.1/bin/hadoop fs -fs hdfs://master:9000 -mkdir /bharath
    hdfs@packer-virtualbox-iso:/vagrant$ hadoop-1.2.1/bin/hadoop fs -fs hdfs://master:9000 -put /vagrant/data/ex1data1.txt /bharath
    hdfs@packer-virtualbox-iso:/vagrant$ hadoop-1.2.1/bin/hadoop fs -fs hdfs://master:9000 -ls /
    Found 2 items
    drwxr-xr-x   - hdfs supergroup          0 2014-05-17 17:07 /bharath
    drwxr-xr-x   - hdfs supergroup          0 2014-05-17 12:06 /user
    hdfs@packer-virtualbox-iso:/vagrant$ hadoop-1.2.1/bin/hadoop fs -fs hdfs://master:9000 -ls /bharath
    Found 1 items
    -rw-r--r--   3 hdfs supergroup       1359 2014-05-17 17:07 /bharath/ex1data1.txt
    ```

9. So by now, we have a working Spark cluster and have placed our data on its HDFS. The next step is to write a Spark application. My application is called *sparkling* and its on github [here](https://github.com/bharath12345/sparkling). You may clone the repository onto your Vagrant Ubuntu box. You will need *sbt* to compile this project. The compilation could take some time. So far I have written just 2 programs in this project. There is one called "Test.scala" which does a simple line-count of the file placed on HDFS in the previous step. You may want to run this, it should print a count of 92 mixed in with a lot of java-logging output. If this program worked then you can run the other program "LocalFileLinearRegression.scala". The command to run these from the sbt prompt is, quite simply -

    ```
    > run-main in.bharathwrites.sparkling.LocalFileLinearRegression
    ```
10. Instead of running the client program from the *sbt* prompt one can build a fat JAR using the *assembly* plugin. Doing so, one can run the client program from the command-line using the well known <code>java -cp jar-name main-class</code>

PS: My project on github (sparkling) is my playground to learn spark. I will keep modifying the code in the coming days. So you may want to once read the code to check and see if it makes sense... as I have a tendency to check-in intermediary non-working code also! :)