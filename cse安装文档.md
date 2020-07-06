# 房角石边缘系统安装说明

[toc]

## 概述

> 房角石边缘系统提供了离线安装包，通过 shell 脚本可以将整个边缘系统部署在用户私有环境。

## 环境准备

### 服务器硬件要求

- 2 核 CPU 和 4GB 内存, 推荐 4 核 CPU 和 8G 内存

- `/data`挂载点剩余空间 20G 以上, 推荐 100G

- 服务器开放 80 端口访问

### 操作系统要求

- CentOS7.X，支持最小化安装版本

- `root`用户权限安装

## 安装包下载

[下载地址](https://cornerstone-app-dev.obs.cn-north-4.myhuaweicloud.com/cse/installer/installer.zip)

## 安装步骤

1. 将下载的安装包 installer.zip 拷贝至任何目录(以下以拷贝到/home目录为例)

2. 进入安装包所在目录

```bash
cd /home
```

3. 解压安装包文件

```bash
unzip installer.zip
```

> [root@localhost home]# ls
> installer.zip
> [root@localhost home]# unzip installer.zip 
> Archive:  installer.zip
>    creating: installer/
>    creating: installer/docker/
>    creating: installer/docker/binary/
>   inflating: installer/docker/binary/docker-19.03.6.tgz
>   inflating: installer/docker/binary/docker-compose
>    creating: installer/docker/files/
>   inflating: installer/docker/files/containerd.service
>   inflating: installer/docker/files/docker.service
>   inflating: installer/docker/files/docker.socket
>    creating: installer/cornerstone/
>   inflating: installer/cornerstone/docker_network_init.sh
>   inflating: installer/cornerstone/docker-compose.yml
>    creating: installer/images/
>   inflating: installer/images/engine_tools.tar.gz
>   inflating: installer/images/cornerstone.tar.gz
>   inflating: installer/install.sh
>   inflating: installer/uninstall.sh

4. 进入解压目录

```bash
cd installer
```

> [root@localhost installer]# ls
> cornerstone  docker  images  install.sh  uninstall.sh

5. 执行install命令

```bash
./install.sh <主机ip>
```

> [root@localhost installer]# ./install.sh 10.129.247.245
> docker/
> docker/containerd
> docker/docker
> docker/ctr
> docker/dockerd
> docker/runc
> docker/docker-proxy
> docker/docker-init
> docker/containerd-shim
> Created symlink from /etc/systemd/system/multi-user.target.wants/containerd.service to /etc/systemd/system/containerd.service.
> Created symlink from /etc/systemd/system/multi-user.target.wants/docker.service to /etc/systemd/system/docker.service.
> Loaded image: packages.glodon.com/docker-cornerstoneplatform-releases/cornerstone-edge-ui:private-dev-1.0.7-SNAPSHOT
> Loaded image: packages.glodon.com/docker-cornerstoneplatform-releases/cornerstone-edge:dev-1.0.8-SNAPSHOT
> Loaded image: packages.glodon.com/docker-cornerstoneplatform-releases/cornerstone-engine:dev-1.0.2
> Loaded image: packages.glodon.com/docker-cornerstoneplatform-releases/mysql-init:dev-1.0.8-SNAPSHOT
> Loaded image: packages.glodon.com/docker-cornerstoneplatform-releases/mysql-debian:5.7
> Loaded image: registry.cn-beijing.aliyuncs.com/infrastructure_midware/rabbitmq-alpine:3.7.8
> Loaded image: packages.glodon.com/docker-cornerstoneplatform-releases/mysql-client:15.1
> Loaded image: packages.glodon.com/docker-cornerstoneplatform-releases/helm:v3.1.2
> Loaded image: packages.glodon.com/docker-cornerstoneplatform-releases/kubectl:v1.15.9
> Loaded image: packages.glodon.com/docker-cornerstoneplatform-releases/curl:7.60.0
> Loaded image: packages.glodon.com/docker-cornerstoneplatform-releases/mongodb:v4.0.6
> Loaded image: packages.glodon.com/docker-cornerstoneplatform-releases/rclone:v1.51.0
> cc5b5f85fccc2a361f847270e36028e0e83a4092195ae95d232cec4c8c51058b
> Creating cornerstone-rabbitmq   ... done
> Creating cornerstone-mysql    ... done
> Creating cornerstone-mysql-init ... done
> Creating cornerstone-engine     ... done
> Creating cornerstone-edge       ... done
> Creating cornerstone-edge-ui    ... done

6. 安装成功后会显示访问地址及用户名和密码

> CSE安装成功!
> 访问地址: http://10.129.247.245
> 用户名:   root
> 密码:     root

7. 如果安装失败, 请提供同目录下的install.log文件
