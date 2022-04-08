package com.wjw.proto;

/**
 * fastdfs协议暂时未分类的常量
 *
 * @author yuqih
 */
public class OtherConstants {

    public static final int FDFS_PROTO_PKG_LEN_SIZE = 8;
    public static final int FDFS_PROTO_CMD_SIZE = 1;
    public static final int FDFS_PROTO_CONNECTION_LEN = 4;
    public static final int FDFS_GROUP_NAME_MAX_LEN = 16;
    public static final int FDFS_IPADDR_SIZE = 16;
    public static final int FDFS_DOMAIN_NAME_MAX_SIZE = 128;
    public static final int FDFS_VERSION_SIZE = 6;
    public static final int FDFS_STORAGE_ID_MAX_SIZE = 16;


    /**
     * 报文头中命令位置
     */
    public static final int PROTO_HEADER_CMD_INDEX = FDFS_PROTO_PKG_LEN_SIZE;
    /**
     * 报文头中状态码位置
     */
    public static final int PROTO_HEADER_STATUS_INDEX = FDFS_PROTO_PKG_LEN_SIZE + 1;

    // 文件扩展名长度
    public static final byte FDFS_FILE_EXT_NAME_MAX_LEN = 6;
    // 文件路径长度
    public static final byte FDFS_FILE_PATH_LEN = 10;

    /**
     * channel共享请求头的key
     */
    public static final String ATTR_KEY_HEAD = "head";
    /**
     * channel共享请求钩子的key
     */
    public static final String ATTR_KEY_FUTURE = "future";
}
