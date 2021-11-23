# 一、项目介绍
my-gfs是一个个人的分布式存储项目。主要是基于dubbo实现。目前项目还在开发当中。[我主要是参考了这个大佬的设计方案](https://gitee.com/maogen_ymg/daisy)。做这个项目主要是想实践一下分布式的项目。
# 二、项目进展
因为项目还没开始几天，所以进度还不是很多，目前的进度如下
- [x] 测试了dubbo
- [x] 完成了master模块的文件系统功能
- [x] 定义了master所需要的rpc接口

下一步想做的：
- [ ] 实现master向文件服务器发送命令的功能
- [ ] 实现master的心跳功能

# 三、目前的一些想法
我在实现文件系统时，首先考虑到如果master宕机了，那么整个文件系统可能就丢失了。所以我希望能够为文件系统实现持久化。

具体来说，主要想模仿zk的持久化方式。在更改文件系统的节点前，先append一条日志，然后再执行相应的操作，这样即使执行操作时master挂掉，后续也可以根据刚刚持久化的日志恢复过来

# 四、项目的运行
目前整个项目是基于springboot 1.5.9以及dubbo开发。运行前只需要配置好zookeeper即可。
我的zookeeper是在docker上配置的。具体的命令如下：


```
1、 拉取镜像
docker pull zookeeper
2、启动镜像映射端口
docker run --name myZookeeper --restart always -e JVMFLAGS="-Xmx1024m" -p 2181:2181 zookeeper
```
配置完成后，直接在idea中运行就可以了
