package com.krcell.classkeeper.utils

import android.content.Context
import android.os.Environment
import android.util.Log
import com.meituan.robust.Patch
import com.meituan.robust.PatchManipulate
import com.meituan.robust.RobustApkHashUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


class PatchManipulateImp : PatchManipulate() {

    /***
     * connect to the network ,get the latest patches
     * l联网获取最新的补丁
     * @param context
     *
     * @return
     */
    override fun fetchPatchList(context: Context): List<Patch> {
        //将app自己的robustApkHash上报给服务端，服务端根据robustApkHash来区分每一次apk build来给app下发补丁
        //apkhash is the unique identifier for  apk,so you cannnot patch wrong apk.
        val robustApkHash = RobustApkHashUtils.readRobustApkHash(context)
        Log.w("robust", "robustApkHash :$robustApkHash")
        //connect to network to get patch list on servers
        //在这里去联网获取补丁列表
        val patch = Patch()
        patch.name = "123"
        //we recommend LocalPath store the origin patch.jar which may be encrypted,while TempPath is the true runnable jar
        //LocalPath是存储原始的补丁文件，这个文件应该是加密过的，TempPath是加密之后的，TempPath下的补丁加载完毕就删除，保证安全性
        //这里面需要设置一些补丁的信息，主要是联网的获取的补丁信息。重要的如MD5，进行原始补丁文件的简单校验，以及补丁存储的位置，这边推荐把补丁的储存位置放置到应用的私有目录下，保证安全性
        patch.localPath = Environment.getExternalStorageDirectory().path + File.separator + "robust" + File.separator + "patch"

        //setPatchesInfoImplClassFullName 设置项各个App可以独立定制，需要确保的是setPatchesInfoImplClassFullName设置的包名是和xml配置项patchPackname保持一致，而且类名必须是：PatchesInfoImpl
        //请注意这里的设置
        patch.patchesInfoImplClassFullName = "com.krcell.classkeeper.utils.PatchesInfoImpl"
        val patches = ArrayList<Patch>()
        patches.add(patch)
        return patches
    }

    /**
     *
     * @param context
     * @param patch
     * @return
     *
     * you can verify your patches here
     */
    override fun verifyPatch(context: Context, patch: Patch): Boolean {
        //do your verification, put the real patch to patch
        //放到app的私有目录
        patch.tempPath = "${context.cacheDir}${File.separator}robust${File.separator}patch"
        //in the sample we just copy the file
        try {
            copy(patch.localPath, patch.tempPath)
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("copy source patch to local patch error, no patch execute in path " + patch.tempPath)
        }

        return true
    }

    @Throws(IOException::class)
    fun copy(srcPath: String, dstPath: String) {
        val src = File(srcPath)
        if (!src.exists()) {
            throw RuntimeException("source patch does not exist ")
        }
        val dst = File(dstPath)
        if (!dst.parentFile.exists()) {
            dst.parentFile.mkdirs()
        }
        val input = FileInputStream(src)
        input.use { input ->
            val out = FileOutputStream(dst)
            out.use { out ->
                var read: Int
                input.use { input ->
                    out.use {
                        while (input.read().also { read = it } != -1) {
                            it.write(read)
                        }
                    }
                }
            }
        }
    }

    /**
     *
     * @param patch
     * @return
     *
     * you may download your patches here, you can check whether patch is in the phone
     */
    override fun ensurePatchExist(patch: Patch): Boolean {
        return true
    }
}