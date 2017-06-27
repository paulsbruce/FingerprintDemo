# This Dockerfile is used to build an image containing basic stuff to be used as a Jenkins slave build node.
FROM ubuntu:trusty
USER root

# Add locales after locale-gen as needed
# Upgrade packages on image
# Preparations for sshd
RUN locale-gen en_US.UTF-8 &&\
    apt-get -q update &&\
    DEBIAN_FRONTEND="noninteractive" apt-get -q upgrade -y -o Dpkg::Options::="--force-confnew" --no-install-recommends &&\
    DEBIAN_FRONTEND="noninteractive" apt-get -q install -y -o Dpkg::Options::="--force-confnew" --no-install-recommends openssh-server &&\
    apt-get -q autoremove &&\
    apt-get -q clean -y && rm -rf /var/lib/apt/lists/* && rm -f /var/cache/apt/*.bin &&\
    sed -i 's|session    required     pam_loginuid.so|session    optional     pam_loginuid.so|g' /etc/pam.d/sshd &&\
    mkdir -p /var/run/sshd

ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8

# Install JDK 8 (latest edition)
RUN apt-get -q update &&\
    DEBIAN_FRONTEND="noninteractive" apt-get -q install -y -o Dpkg::Options::="--force-confnew" --no-install-recommends software-properties-common &&\
    add-apt-repository -y ppa:openjdk-r/ppa &&\
    apt-get -q update &&\
    DEBIAN_FRONTEND="noninteractive" apt-get -q install -y -o Dpkg::Options::="--force-confnew" --no-install-recommends openjdk-8-jre-headless &&\
    apt-get -q clean -y && rm -rf /var/lib/apt/lists/* && rm -f /var/cache/apt/*.bin

# Set user jenkins to the image
RUN useradd -m -d /home/jenkins -s /bin/sh jenkins &&\
    echo "jenkins:jenkins" | chpasswd

# Standard SSH port
EXPOSE 22

# Default command
CMD ["/usr/sbin/sshd", "-D"]



## create writable jenkins directories for the initial install
RUN mkdir /var/log/jenkins
RUN mkdir /var/cache/jenkins
RUN chown -R jenkins:jenkins /var/log/jenkins
RUN chown -R jenkins:jenkins /var/cache/jenkins

## install basic distro tools used by post-build automation scripts
RUN apt-get update
RUN apt-get install -y apt-transport-https
RUN apt-get -q -y install lsof
RUN apt-get install -y wget

## get latest versions at https://developer.android.com/studio/index.html
RUN wget https://dl.google.com/android/repository/tools_r25.2.3-linux.zip -O /opt/android-sdk.zip
RUN mkdir /opt/android-sdk-linux
RUN chmod -R 755 /opt/android-sdk-linux
RUN unzip /opt/android-sdk.zip -d /opt/android-sdk-linux
RUN rm /opt/android-sdk.zip

#RUN wget https://dl.google.com/android/repository/android-ndk-r14-linux-x86_64.zip -O /opt/android-ndk.zip
#RUN mkdir /opt/android-sdk-linux/android-ndk
#RUN chmod -R 755 /opt/android-sdk-linux/android-ndk
#RUN unzip /opt/android-ndk.zip -d /opt/android-sdk-linux/android-ndk
#RUN rm /opt/android-ndk.zip

## configure profile and environment variables
RUN >/etc/profile.d/android.sh
RUN sed -i '$ a\export ANDROID_HOME="/opt/android-sdk-linux"' /etc/profile.d/android.sh
RUN sed -i '$ a\export PATH="$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools:$PATH"' /etc/profile.d/android.sh
RUN . /etc/profile

## install system-wide git tools
RUN apt-get install git-core

## install Android SDK, auto-accepting terms of service
RUN ( sleep 5 && while [ 1 ]; do sleep 1; echo y; done ) | /opt/android-sdk-linux/tools/android update sdk --no-ui --all --filter platform-tools,android-25,build-tools-26.0.0,build-tools-25.0.2,android-24,build-tools-24.0.1,android-23,build-tools-23.0.3,android-21,build-tools-21.1.2,tools,extra-android-support,extra-android-m2repository

RUN dpkg --add-architecture i386
USER jenkins
ENV JAVA_OPTS="-Xmx8192m"
