<!--ts-->
   * [使用roles组装快速构建应用部署playbook](#使用roles组装快速构建应用部署playbook)
         * [背景](#背景)
         * [痛点](#痛点)
         * [方案](#方案)
         * [roles引擎目录结构示例](#roles引擎目录结构示例)
         * [组装后的完整playbook目录结构示例](#组装后的完整playbook目录结构示例)
         * [接口文件格式示例](#接口文件格式示例)
         * [已提供的roles引擎列表](#已提供的roles引擎列表)
         * [ansible 引擎要求](#ansible-引擎要求)
   * [MySQL初始化role引擎](#mysql初始化role引擎)
         * [功能说明](#功能说明)
         * [使用说明](#使用说明)
         * [文件/目录说明](#文件目录说明)
         * [参数说明](#参数说明)
         * [ansible引擎依赖](#ansible引擎依赖)
   * [MongoDB初始化role引擎](#mongodb初始化role引擎)
         * [功能说明](#功能说明-1)
         * [使用说明](#使用说明-1)
         * [文件/目录说明](#文件目录说明-1)
         * [参数说明](#参数说明-1)
         * [ansible引擎依赖](#ansible引擎依赖-1)
   * [Docker镜像初始化role引擎](#docker镜像初始化role引擎)
         * [功能说明](#功能说明-2)
         * [使用说明](#使用说明-2)
         * [文件/目录说明](#文件目录说明-2)
         * [参数说明](#参数说明-2)
   * [基于k8s的应用初始化role引擎](#基于k8s的应用初始化role引擎)
         * [功能说明](#功能说明-3)
         * [使用说明](#使用说明-3)
         * [文件/目录说明](#文件目录说明-3)
         * [参数说明](#参数说明-3)
         * [ansible引擎依赖](#ansible引擎依赖-2)
   * [coredns初始化role引擎](#coredns初始化role引擎)
         * [功能说明](#功能说明-4)
         * [使用说明](#使用说明-4)
         * [文件/目录说明](#文件目录说明-4)
   * [Apollo初始化role引擎](#apollo初始化role引擎)
         * [功能说明](#功能说明-5)
         * [使用说明](#使用说明-5)
         * [文件/目录说明](#文件目录说明-5)
         * [参数说明](#参数说明-4)
         * [ansible引擎依赖](#ansible引擎依赖-3)

<!-- Added by: root, at: Thu May 14 02:02:57 UTC 2020 -->

<!--te-->
# 使用`roles`组装快速构建应用部署`playbook`

### 背景

- 业务应用通常依赖若干中间件，在业务应用部署前可能需要对中间件进行初始化
- 业务应用部署过程中通常需要处理大量的配置文件以及资源模板文件
- 使用房角石系统部署业务应用时，需要把整个部署流程转化为`ansible-playbook`脚本

### 痛点

- 由于应用的差异性，针对每个业务应用都需要写一套`ansible-playbook`脚本
- `ansible-playbook`脚本带来较多的学习成本
- 写一个具备幂等性、兼容性、可扩展性的`ansible-playbook`需要较多经验

### 方案

- 针对常用的中间件初始化需求以及应用部署需求提供通用的`roles`引擎
- 用户不用写一行代码，只需按照规则，通过对`roles`文件或内容的复制粘贴，来组装出完整的`playbook`脚本

### `roles`引擎目录结构示例

```bash
.
├── app_init.yaml       # playbook文件，roles执行的入口文件
├── hosts.yaml          # 主机清单接口文件，用于房角石系统上架时提取
├── inventory           # 主机清单文件，用于本地测试
├── properties.yaml     # 参数接口文件，用于房角石系统上架时提取
├── res                 # 数据文件根目录，存放所有用于初始化的数据文件以及用于应用部署的资源模板文件等
│   ├── app_init        # 数据文件子目录，对应同名role所需数据文件
│   ├── app_init_helm
│   └── kubeconfig      # 特殊文件目录，存放应用部署过程中可能用的其他文件
└── roles               # roles引擎核心代码目录，一般情况下无需关注
    ├── app_init
    └── app_init_helm
```

### 组装后的完整`playbook`目录结构示例

```bash
.
├── coredns_init.yaml       # coredns_init role执行的入口文件
├── hosts.yaml              # 主机清单接口文件，由roles的hosts.yaml组装而成
├── images_init.yaml        # images_init role执行的入口文件
├── inventory               # 主机清单文件，用于本地测试
├── k8s_app_init.yaml       # app_init role执行的入口文件
├── mysql_init.yaml         # mysql_init role执行的入口文件
├── playbook.yaml           # roles执行的主入口文件
├── properties.yaml         # 参数接口文件，由roles的properties.yaml组装而成，用于渲染的变量也在此
├── res                     # 数据文件根目录，由roles的res目录组装而成
│   ├── coredns_init        # coredns_init role所需数据文件目录
│   ├── images_init         # images_init role所需数据文件目录
│   ├── k8s_app_init        # app_init role所需数据文件目录
│   ├── k8s_app_init_helm   # app_init_helm role所需数据文件目录
│   ├── kubeconfig          # k8s部署所需kubeconfig文件目录
│   └── mysql_init          # mysql_init role所需数据文件目录
├── roles                   # roles引擎核心代码目录，由roles的roles目录组装而成，一般情况下无需关注
│   ├── coredns_init
│   ├── images_init
│   ├── k8s_app_init
│   ├── k8s_app_init_helm
│   └── mysql_init
└── stages.yaml             # 分步部署接口文件，用于房角石系统分步部署功能
```

### 接口文件格式示例

- `hosts.yaml`

```bash
kube_master:                               # 主机清单分组名称key，不能重复
  label: k8s集群master节点分组               # 在房角石系统上的显示名称，用于为应用实例绑定主机节点
  description: 选择k8s集群所有master节点主机
kube_node:
  label: k8s集群node节点分组
  description: 选择k8s集群所有node节点主机
```

- `playbook.yaml`

```bash
- import_playbook: mysql_init.yaml         # 根据应用情况按顺序导入所有roles执行入口文件
- import_playbook: coredns_init.yaml
- import_playbook: images_init.yaml
- import_playbook: k8s_app_init.yaml
```

- `properties.yaml`

```bash
# app_init
- key: k8s_app_init_k8s_namespace          # 参数名称，对应待渲染文件中的变量名称，不能重复
  label: k8s namespace                     # 参数在房角石系统上的显示名称
  value: glodon-taier                      # 参数默认值
  type: string                             # 参数类型，目前只支持string
  required: 1                              # 是否必填，1为必填，0为非必填
  description: 应用所在的k8s namespace       # 参数描述
- key: k8s_app_init_kubeconfig_path
  label: kubeconfig文件地址
  value:
  type: string
  required: 1
  description: kubeconfig文件地址, 支持目录和url
- key: k8s_app_init_kubeconfig_user
  label: kubeconfig下载用户
  value:
  type: string
  required: 0
  description: 当kubeconfig文件地址为url时的下载用户名
- key: k8s_app_init_kubeconfig_passwd
  label: kubeconfig下载密码
  value:
  type: string
  required: 0
  description: 当kubeconfig文件地址为url时的下载密码
```

- `stages.yaml`

```bash
- stage: 01:mysql初始化                        # 分步部署步骤名称
  playbook: mysql_init.yaml                   # 对应的role执行入口文件名称
  description: 创建数据库，导入数据最小集sql文件    # 步骤描述
- stage: 02:coredns初始化
  playbook: coredns_init.yaml
  description: 将ip和url写入k8s集群master节点的/etc/hosts, 并重启coredns
- stage: 03:images初始化
  playbook: images_init.yaml
  description: 将docker images导入到k8s集群node节点
- stage: 04:app初始化
  playbook: k8s_app_init.yaml
  description: 创建k8s资源, 并校验资源状态
```

### 已提供的`roles`引擎列表

| roles 名称   | 用途                                                               | 下载地址 |
| ------------ | ------------------------------------------------------------------ | -------- |
| mysql_init   | MySQL 初始化                                                       |          |
| mongodb_init | MongoDB 初始化                                                     |          |
| images_init  | 为主机节点导入 docker 镜像                                         |          |
| k8s_app_init | 部署基于 k8s 的应用                                                |          |
| coredns_init | 将 ip 和 url 写入 k8s 集群 master 节点的/etc/hosts, 并重启 coredns |          |
| apollo_init  | Apollo 初始化                                                      |          |

### ansible 引擎要求

- ansible 引擎容器化部署时，需挂载宿主机的/var/run/docker.sock 文件
- 安装`Docker`客户端

---

# `MySQL`初始化`role`引擎

### 功能说明

1. 使用 MySQL 逻辑备份初始化 MySQL 数据库
2. 执行额外的 sql 语句

### 使用说明

1. 数据文件数量不限，目录层级不限，放在 res/mysql_init/目录下即可

2. 支持的数据文件扩展名：`*.sql`、`*.j2`、`*.zip`/`*.tar`/`*.tar.gz`/`tar.xz`/`tar.bz2`

3. 不同扩展名的文件用途及要求

- **`*.sql`**：要导入的 sql 文件，以**数据库名称**命名，不同子目录下文件名称不能冲突
- **`*.j2`**：需要渲染并额外执行的 sql 语句文件，每个语句独占一行，文件名称不限
- **`*.zip`/`*.tar`/`*.tar.gz`/`tar.xz`/`tar.bz2`**：数据文件压缩包，名称不限，解压后只处理`*.sql`和`*.j2`文件，其他文件将被忽略

### 文件/目录说明

| 文件名/目录名称   | 用途             | 是否需要组装                         |
| ----------------- | ---------------- | ------------------------------------ |
| hosts.yaml        | 主机清单接口文件 | _否_                                 |
| inventory         | 主机清单文件     | _否_                                 |
| mysql_init.yaml   | playbook 文件    | 是                                   |
| properties.yaml   | 参数接口文件     | 是，组装文件内容，注意处理冲突的 key |
| res/mysql_init/   | 数据文件存放目录 | 是                                   |
| roles/mysql_init/ | roles 文件目录   | 是                                   |

### 参数说明

| 参数名称               | 描述                                               | 是否必填             |
| ---------------------- | -------------------------------------------------- | -------------------- |
| mysql_init_host        | 待数据初始化的 mysql 实例地址, 必须为 ip 或域名    | 是                   |
| mysql_init_port        | mysql 实例端口号                                   | 是                   |
| mysql_init_root_user   | mysql 实例管理员用户名, 需要有管理数据库对象的权限 | 是                   |
| mysql_init_root_passwd | mysql 实例管理员密码                               | 是                   |
| mysql_init_user        | mysql 实例应用用户名称, 用于业务应用               | 是                   |
| mysql_init_passwd      | mysql 实例应用用户密码, 用于业务应用               | 是                   |
| ansible_engine_image   | ansible 引擎镜像名称                               | 是，组装后只保留一个 |

### `ansible`引擎依赖

- 安装 Python 模块：`docker`、`PyMySQL`
- docker 镜像：packages.glodon.com/docker-cornerstoneplatform-releases/mysql-client:15.1

---

# `MongoDB`初始化`role`引擎

### 功能说明

使用 MongoDB 备份集初始化 MongoDB 数据库

### 使用说明

1. 数据文件目录名称以数据库名称命名，目录数量不限，放在 res/mongodb_init/目录下即可

2. 数据文件目录下仅可以包含`*.bson`和`*.json`文件，且不能有子目录

3. 支持数据文件为压缩包格式，扩展名支持`*.zip`/`*.tar`/`*.tar.gz`/`tar.xz`/`tar.bz2`，压缩包名称不限，数量不限，目录层级不限，不能与其他扩展名的文件并存，解压后的文件要求见以上两条

4. 数据文件目录结构示例

   ```bash
   mongodb_init
   └── enno
       ├── Rule.bson
       ├── Rule.metadata.json
       ├── weatherAlarmRecordState.bson
       └── weatherAlarmRecordState.metadata.json
   ```

### 文件/目录说明

| 文件名/目录名称     | 用途             | 是否需要组装                         |
| ------------------- | ---------------- | ------------------------------------ |
| hosts.yaml          | 主机清单接口文件 | _否_                                 |
| inventory           | 主机清单文件     | _否_                                 |
| mongodb_init.yaml   | playbook 文件    | 是                                   |
| properties.yaml     | 参数接口文件     | 是，组装文件内容，注意处理冲突的 key |
| res/mongodb_init/   | 数据文件存放目录 | 是                                   |
| roles/mongodb_init/ | roles 文件目录   | 是                                   |

### 参数说明

| 参数名称             | 描述                                   | 是否必填             |
| -------------------- | -------------------------------------- | -------------------- |
| mongodb_init_uri     | 待数据初始化的 mongodb 实例 uri        | 是                   |
| mongodb_init_user    | mongodb 实例应用用户名称, 用于业务应用 | 是                   |
| mongodb_init_passwd  | mongodb 实例应用用户密码, 用于业务应用 | 是                   |
| ansible_engine_image | ansible 引擎镜像名称                   | 是，组装后只保留一个 |

### `ansible`引擎依赖

- 安装 Python 模块：`docker`
- docker 镜像：packages.glodon.com/docker-cornerstoneplatform-releases/mongodb:v4.0.6

---

# `Docker`镜像初始化`role`引擎

### 功能说明

针对无网环境，在不使用 Harbor 的场景，把所需的 docker 镜像导入到 k8s 集群所有 node 节点

### 使用说明

1. 数据文件数量不限，文件名称不限，目录层级不限，放在 res/images_init/目录下即可
2. 支持的数据文件扩展名：`*.tar`
3. 支持指定 url 地址下载数据文件，数据文件扩展名为`*.tar`即可

### 文件/目录说明

| 文件名/目录名称    | 用途             | 是否需要组装                         |
| ------------------ | ---------------- | ------------------------------------ |
| hosts.yaml         | 主机清单接口文件 | 是                                   |
| inventory          | 主机清单文件     | _否_                                 |
| images_init.yaml   | playbook 文件    | 是                                   |
| properties.yaml    | 参数接口文件     | 是，组装文件内容，注意处理冲突的 key |
| res/images_init/   | 数据文件存放目录 | 是                                   |
| roles/images_init/ | roles 文件目录   | 是                                   |

### 参数说明

| 参数名称                  | 描述                      | 是否必填 |
| ------------------------- | ------------------------- | -------- |
| images_init_download_path | docker 镜像压缩包下载地址 | _否_     |

---

# 基于`k8s`的应用初始化`role`引擎

### 功能说明

1. 使用`kubectl`部署 k8s 资源文件
2. 使用`helm`部署 Chart 资源文件

### 使用说明

- `res/k8s_app_init`

1. 存放使用`kubectl`部署的资源文件或模板，扩展名支持`*.yaml`/`*.j2`

2. 部署顺序：优先目录层级深度，其次是文件名称字典序，目录结构示例如下，部署顺序为从上到下

   ```bash
   .
   ├── namespace.yaml.j2
   ├── 01-secret
   │   └── cloudt-secret.yaml.j2
   ├── 02-configmap
   │   ├── apm-config.yaml.j2
   |   ├── ......
   │   └── bim5d-eureka-service.yaml.j2
   ├── 04-report
   │   ├── report-config.yml.j2
   |   ├── ......
   │   └── reportoptions-config.yaml.j2
   ├── 05-svc
   │   ├── bim5d-eureka-service.yaml.j2
   |   ├── ......
   │   └── work-weixin-service.yaml.j2
   ├── deployment
   │   ├── 01-discovery
   │   │   ├── morrow-infra-discovery-ha1.yaml.j2
   │   │   ├── morrow-infra-discovery-ha2.yaml.j2
   │   │   └── morrow-infra-discovery-ha3.yaml.j2
   │   ├── 02-config
   │   │   ├── morrow-infra-config.yaml.j2
   │   │   └── project-config-service.yaml.j2
   │   ├── 03-gatewayadmin
   │   │   ├── gateway-admin-service.yaml.j2
   │   │   └── morrow-infra-admin.yaml.j2
   │   ├── 04-service
   │   │   ├── aggregated-service.yaml.j2
   │   │   ├── base
   │   │   │   ├── public-role-service.yaml.j2
   │   │   │   └── user-service.yaml.j2
   │   │   ├── bim5d-upgrade-service.yaml.j2
   │   │   └── workweixin-sync-job.yaml.j2
   │   └── 05-web
   │       ├── gateway-web.yaml.j2
   │       └── videomonitor-web.yaml.j2
   └── ingress
       ├── bim5d-eureka-service.yaml.j2
       └── work-weixin-service.yaml.j2

   ```

3. 支持数据文件为压缩包格式，扩展名支持`*.zip`/`*.tar`/`*.tar.gz`/`tar.xz`/`tar.bz2`，压缩包名称不限，数量不限，目录层级不限，不能与其他扩展名的文件并存，解压后的文件要求见以上两条

- `res/k8s_app_init_helm`

1. 存放使用`helm`部署的 Chart 模板和 values 模板文件

2. Chart 模板目录和 values 模板文件放在同级目录，values 模板文件与 Chart 模板目录为一对一或多对一关系

3. `RELEASE`名称为 values 模板文件名称

4. 部署顺序：优先目录层级深度，其次是 values 模板文件名称字典序，目录结构示例如下，部署顺序为从上到下

   ```bash
   .
   ├── digiarch                            # Chart模板
   │   ├── Chart.yaml
   │   ├── templates
   │   └── values.yaml
   ├── platform-foundation-local.yaml.j2   # 对应digiarch的values模板文件1
   ├── platform-portal-local.yaml.j2       # 对应digiarch的values模板文件2
   ├── platform-sp-demo-local.yaml.j2      # 对应digiarch的values模板文件3
   ├── platform-static-local.yaml.j2       # 对应digiarch的values模板文件4
   └── test
       ├── test                            # Chart模板
       └── test-value.yaml.j2              # 对应test的values模板文件
   ```

5. 支持数据文件为压缩包格式，扩展名支持`*.zip`/`*.tar`/`*.tar.gz`/`tar.xz`/`tar.bz2`，压缩包名称不限，数量不限，目录层级不限，不能与其他扩展名的文件并存，解压后的文件要求见以上四条

- 所有资源模板中用到的参数都需要在 properties.yaml 中定义

### 文件/目录说明

| 文件名/目录名称          | 用途                                | 是否需要组装                       |
| ------------------------ | ----------------------------------- | ---------------------------------- |
| hosts.yaml               | 主机清单接口文件                    | _否_                               |
| inventory                | 主机清单文件                        | _否_                               |
| k8s_app_init.yaml        | playbook 文件                       | 是                                 |
| properties.yaml          | 参数接口文件                        | 是，组装文件内容，注意处理冲突 key |
| res/k8s_app_init/        | 使用 kubectl 部署的资源文件存放目录 | 是                                 |
| res/k8s_app_init_helm/   | 使用 helm 部署的资源文件存放目录    | 是                                 |
| res/kubeconfig/          | kubeconfig 文件存放目录             | 是                                 |
| roles/k8s_app_init/      | roles 文件目录（kubectl 部署）      | 是                                 |
| roles/k8s_app_init_helm/ | roles 文件目录（helm 部署）         | 是                                 |

### 参数说明

| 参数名称                       | 描述                                        | 是否必填             |
| ------------------------------ | ------------------------------------------- | -------------------- |
| k8s_app_init_k8s_namespace     | 应用所在的 k8s namespace 名称               | 是                   |
| k8s_app_init_kubeconfig_path   | kubeconfig 文件地址, 支持目录和 url         | 是                   |
| k8s_app_init_kubeconfig_user   | 当 kubeconfig 文件地址为 url 时的下载用户名 | _否_                 |
| k8s_app_init_kubeconfig_passwd | 当 kubeconfig 文件地址为 url 时的下载密码   | _否_                 |
| ansible_engine_image           | ansible 引擎镜像名称                        | 是，组装后只保留一个 |

### `ansible`引擎依赖

- 安装 Python 模块：`docker`

- docker 镜像

  ```bash
  packages.glodon.com/docker-cornerstoneplatform-releases/kubectl:v1.15.9
  packages.glodon.com/docker-cornerstoneplatform-releases/helm:v3.1.2
  ```

---

# `coredns`初始化`role`引擎

### 功能说明

使用 k8s 集群 master 节点本地 hosts 解析结合 coredns，来实现 k8s 内部服务通过域名交互

### 使用说明

1. 数据文件数量不限，文件名称不限，目录层级不限，放在 res/coredns_init/目录下即可

2. 支持的数据文件扩展名：`*.j2`，内容为要写入的 hosts 本地解析，每个解析条目独占一行。文件格式示例：

```bash
{{ k8s_vip }} {{ frontend_domain }}
{{ k8s_vip }} {{ backend_domain }}
```

3. 支持数据文件为压缩包格式，扩展名支持`*.zip`/`*.tar`/`*.tar.gz`/`tar.xz`/`tar.bz2`，压缩包名称不限，数量不限，目录层级不限，解压后的文件要求见以上两条

4. 所有用到的变量参数都需要在 properties.yaml 中定义

### 文件/目录说明

| 文件名/目录名称     | 用途             | 是否需要组装                         |
| ------------------- | ---------------- | ------------------------------------ |
| hosts.yaml          | 主机清单接口文件 | 是                                   |
| inventory           | 主机清单文件     | _否_                                 |
| coredns_init.yaml   | playbook 文件    | 是                                   |
| properties.yaml     | 参数接口文件     | 是，组装文件内容，注意处理冲突的 key |
| res/coredns_init/   | 数据文件存放目录 | 是                                   |
| roles/coredns_init/ | roles 文件目录   | 是                                   |

---

# `Apollo`初始化`role`引擎

### 功能说明

使用模板文件调整 Apollo 中的配置信息

### 使用说明

1. 数据文件数量不限，文件名称不限，目录层级不限，放在 res/apollo_init/目录下即可

2. 支持的数据文件扩展名：`*.j2`，内容为要调整的 Apollo 配置信息，每个条目独占一行。格式示例及说明：

```bash
enno-common##enno.redis##spring.redis.host##{{ spring_redis_host }}##redis地址
enno-common##enno.redis##spring.redis.port##{{ spring_redis_port }}##redis端口
enno-common##enno.redis##spring.redis.password##{{ spring_redis_password }}##redis密码
```

| appid       | namespace  | key                   | value                       | 备注       |
| ----------- | ---------- | --------------------- | --------------------------- | ---------- |
| enno-common | enno.redis | spring.redis.host     | {{ spring_redis_host }}     | redis 地址 |
| enno-common | enno.redis | spring.redis.port     | {{ spring_redis_port }}     | redis 端口 |
| enno-common | enno.redis | spring.redis.password | {{ spring_redis_password }} | redis 密码 |

3. 支持数据文件为压缩包格式，扩展名支持`*.zip`/`*.tar`/`*.tar.gz`/`tar.xz`/`tar.bz2`，压缩包名称不限，数量不限，目录层级不限，解压后的文件要求见以上两条

4. 所有用到的变量参数都需要在 properties.yaml 中定义

### 文件/目录说明

| 文件名/目录名称     | 用途             | 是否需要组装                         |
| ------------------- | ---------------- | ------------------------------------ |
| hosts.yaml          | 主机清单接口文件 | 是                                   |
| inventory           | 主机清单文件     | _否_                                 |
| coredns_init.yaml   | playbook 文件    | 是                                   |
| properties.yaml     | 参数接口文件     | 是，组装文件内容，注意处理冲突的 key |
| res/coredns_init/   | 数据文件存放目录 | 是                                   |
| roles/coredns_init/ | roles 文件目录   | 是                                   |

### 参数说明

| 参数名称             | 描述                                        | 是否必填             |
| -------------------- | ------------------------------------------- | -------------------- |
| apollo_init_host     | apollo portal 的 ip 地址, 一般为 k8s 的 vip | 是                   |
| apollo_init_domain   | apollo portal 的 url 访问地址               | 是                   |
| apollo_init_env      | Apollo 环境名称(PRO/UAT 等）                | 是                   |
| ansible_engine_image | ansible 引擎镜像名称                        | 是，组装时只保留一个 |

### `ansible`引擎依赖

- 安装 Python 模块：`docker`
- docker 镜像：packages.glodon.com/docker-cornerstoneplatform-releases/curl:7.60.0
