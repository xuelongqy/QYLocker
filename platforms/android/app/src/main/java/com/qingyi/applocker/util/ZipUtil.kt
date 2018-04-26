package com.qingyi.applocker.util

import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile


object ZipUtil {
    fun upZipFileDir(zfile: ZipFile, folderPath: String): Boolean {
        // 判断文件夹是否存在,不存在则创建
        if (!File(folderPath).exists()) {
            File(folderPath).mkdir()
        }
        val zList = zfile.entries()
        var ze: ZipEntry? = null
        val buf = ByteArray(1024)
        while (zList.hasMoreElements()) {
            ze = zList.nextElement() as ZipEntry
            //列举的压缩文件里面的各个文件，判断是否为目录
            if (ze.isDirectory) {
                val dirstr = folderPath + File.separator + ze.name
                dirstr.trim { it <= ' ' }
                val f = File(dirstr)
                f.mkdir()
                continue
            }
            var os: OutputStream? = null
            var fos: FileOutputStream? = null
            // ze.getName()会返回 script/start.script这样的，是为了返回实体的File
            val realFile = getRealFileName(folderPath, ze.name)
            try {
                fos = FileOutputStream(realFile)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                return false
            }

            os = BufferedOutputStream(fos)
            var `is`: InputStream? = null
            try {
                `is` = BufferedInputStream(zfile.getInputStream(ze))
            } catch (e: IOException) {
                e.printStackTrace()
                return false
            }

            var readLen = 0
            //进行一些内容复制操作
            try {
                while ({readLen = `is`.read(buf, 0, 1024);readLen}() != -1) {
                    os.write(buf, 0, readLen)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                return false
            }

            try {
                `is`.close()
                os.close()
            } catch (e: IOException) {
                e.printStackTrace()
                return false
            }

        }
        try {
            zfile.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }

        return true
    }

    /**
     * 给定根目录，返回一个相对路径所对应的实际文件名.
     * @param baseDir 指定根目录
     * @param absFileName 相对路径名，来自于ZipEntry中的name
     * @return java.io.File 实际的文件
     */
    fun getRealFileName(baseDir: String, absFileName: String): File {
        val dirs = absFileName.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var ret = File(baseDir)
        var substr: String? = null

        if (dirs.size > 1) {
            for (i in 0 until dirs.size - 1) {
                substr = dirs[i]
                ret = File(ret, substr)
            }

            if (!ret.exists())
                ret.mkdirs()
            substr = dirs[dirs.size - 1]
            ret = File(ret, substr)
            return ret
        } else {
            ret = File(ret, absFileName)
        }
        return ret
    }
}