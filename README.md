## 项目介绍

netty写的文件服务器（学习netty用），抄fastdfs就对了。还抄了tobato:fastdfs-client

能力有限，有些东西抄一半，还请原谅。。。

### 自定义协议
协议包由两部分组成：header和body，请求与响应都使用相同协议

[FastDFS 服务端与客户端之间的通讯协议（自定义的通讯协议）_keep-go-on的博客-CSDN博客_fastdfs 协议](https://keepgoon.blog.csdn.net/article/details/106877138)

1. header共10字节（与fastdfs一样）
   1. 8字节表示body长度
   2. 1字节表示命令
   3. 1字节表示状态
2. body根据不同命令各自处理（与fastdfs不同）

### API

- [ ] 普通文件上传

  1. 请求参数

     fileSize + fileExtName + fileData

     fileSize：文件长度，8字节

     fileExtName：不包括小数点的文件扩展名，6字节

     fileData：文件数据

  2. 响应参数

     path：文件保存路径（非真实路径，可以认为是文件id）

- [ ] 断点续传
