package net.liuxuan.utils;

//~--- JDK imports ------------------------------------------------------------
import java.io.*;

public class FilePlus {

    /**
     * 仅列出目录的FileFilter
     */
    public static FileFilter dirOnlyFilter = new FileFilter() {

        public boolean accept(File pathname) {
            if (pathname.isDirectory()) {
                return true;
            }
//          String tmp=pathname.getName().toLowerCase();
//          if(tmp.endsWith(".jar") || tmp.endsWith(".zip")){
//              return true;
//          }
            return false;
        }
    };
    /**
     * 仅列出文件的FileFilter
     */
    public static FileFilter fileOnlyFilter = new FileFilter() {

        public boolean accept(File pathname) {
            if (pathname.isFile()) {
                return true;
            }
            return false;
        }
    };
    /**
     * 不列出CVS目录的目录FileFilter
     */
    public static FileFilter nocvs_DirOnlyFilter = new FileFilter() {

        public boolean accept(File pathname) {
            if (pathname.isDirectory()) {
                if (!pathname.getName().equalsIgnoreCase("cvs")) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }
    };
    /**
     * 不列出CVS目录的目录FileFilter
     */
    public static FileFilter cvs_DirOnlyFilter = new FileFilter() {

        public boolean accept(File pathname) {
            if (pathname.isDirectory()) {
                if (pathname.getName().equalsIgnoreCase("cvs")) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }
    };
    /**
     * 仅列出含CVS目录的目录FileFilter
     */
    public static FileFilter cvsFather_DirOnlyFilter = new FileFilter() {

        public boolean accept(File pathname) {
            if (pathname.isDirectory()) {
                if (!pathname.getName().equalsIgnoreCase("cvs")) {
                    File[] tempfiles = pathname.listFiles(cvs_DirOnlyFilter);
                    if (tempfiles != null && tempfiles.length > 0) {
                        return true;
                    }
                }
            }
            return false;
        }
    };

    /**
     * 复制文件,未做目标存在等验证。
     *
     * @param src 源文件
     * @param dest 目标文件
     */
    public static void CopyFile(File src, File dest) throws IOException {
        FileInputStream fis = new FileInputStream(src);
        FileOutputStream fos = new FileOutputStream(dest);
        byte[] buff = new byte[1024];
        int readed = -1;
        while ((readed = fis.read(buff)) > 0) {
            fos.write(buff, 0, readed);
        }
        fis.close();
        fos.close();
    }

    /**
     * 读一个文本文件到一个字符串，无换行
     *
     * @param path 文件地址
     * @return
     */
    public static String ReadTextFileToString(String path) {
        return ReadTextFileToString(path, null);
    }

    /**
     * 读一个文本文件到一个字符串，有换行
     *
     * @param path 文件地址
     * @return
     */
    public static String ReadTextFileToStringLn(String path) {
        return ReadTextFileToString(path, "\r\n");
    }
    
    /**
     * 读一个文本文件到一个字符串，有换行
     *
     * @param path 文件
     * @return
     */
    public static String ReadTextFileToStringLn(File path) {
        return ReadTextFileToString(path, "\r\n");
    }
    
    /**
     * 读一个文本文件到一个字符串
     *
     * @param path 文件地址
     * @param suffix 每行间隔符
     * @return
     */
    public static String ReadTextFileToString(String path, String suffix) {
        File file = new File(path);

        return ReadTextFileToString(file,suffix);
    }
    /**
     * 读一个文本文件到一个字符串
     *
     * @param path 文件地址
     * @param suffix 每行间隔符
     * @return
     */
    public static String ReadTextFileToString(File file, String suffix) {
    	return ReadTextFileToString(file,suffix,"utf-8");
    }
    /**
     * 读一个文本文件到一个字符串
     *
     * @param path 文件地址
     * @param suffix 每行间隔符
     * @param charset 字符集
     * @return
     */
    public static String ReadTextFileToString(File file, String suffix,String charset) {
//        File file = new File(path);

//      System.out.println(file.getAbsolutePath());
//      System.out.println(file.getCanonicalPath());
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();

        try {

            // System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),charset));

            String tempString = null;

            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString);
//                System.out.println(tempString);
                if (suffix != null) {
                    sb.append(suffix);
                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }

        return sb.toString();
    }

    /**
     * 读取文本内容
     *
     * @param filename 文本名称
     * @return
     */
    public static String readText(String filename) {
        File file = new File(filename);

        try {
            BufferedReader br = new BufferedReader(new java.io.FileReader(file));
            StringBuffer sb = new StringBuffer();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }

            br.close();

            return sb.toString();
        } catch (IOException e) {

//          LogInfo.error(this.getClass().getName(), e.getLocalizedMessage(), e);
            e.printStackTrace();

            return null;
        }
    }

    /**
     * 将内容写到文本中
     *
     * @param textPath　文件路径
     * @param textname　文件名称
     * @param date 写入的内容
     * @return
     */
    public static boolean writeText(String textPath, String textname, String data) {
        boolean flag = false;
        File filePath = new File(textPath);

        if (!filePath.exists()) {
            filePath.mkdirs();
        }

        try {
            FileWriter fw = new FileWriter(textPath + File.separator + textname);

            fw.write(data);
            flag = true;

            if (fw != null) {
                fw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return flag;
    }

    /**
     * 将内容写到文本中
     *
     * @param textPath　文件路径
     * @param textname　文件名称
     * @param date 写入的内容
     * @return
     */
    public static boolean writeTextAnsi(String textPath, String textname, String data) {
        boolean flag = false;
        File filePath = new File(textPath);

        if (!filePath.exists()) {
            filePath.mkdirs();
        }

        try {
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(textPath + File.separator + textname), "GBK");

            osw.write(data);
            flag = true;

            if (osw != null) {
                osw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return flag;
    }

    public static boolean createFile(String destFileName) {
        File file = new File(destFileName);
        return createFile(file);
    }

    public static boolean createFile(File file) {
        String destFileName = file.getAbsolutePath();
//        File file = new File(destFileName);
        if (file.exists()) {
//            System.out.println("创建单个文件" + destFileName + "失败，目标文件已存在！");
            return false;
        }
        if (destFileName.endsWith(File.separator)) {
//            System.out.println("创建单个文件" + destFileName + "失败，目标文件不能为目录！");
            return false;
        }
        //判断目标文件所在的目录是否存在
        if (!file.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
//            System.out.println("目标文件所在目录不存在，准备创建它！");
            if (!file.getParentFile().mkdirs()) {
//                System.out.println("创建目标文件所在目录失败！");
                return false;
            }
        }
        //创建目标文件
        try {
            if (file.createNewFile()) {
//                System.out.println("创建单个文件" + destFileName + "成功！");
                return true;
            } else {
//                System.out.println("创建单个文件" + destFileName + "失败！");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
//            System.out.println("创建单个文件" + destFileName + "失败！" + e.getMessage());
            return false;
        }
    }

    /**
     * 将内容写到文本中
     *
     * @param filename　文件名称
     * @param data 写入的内容
     * @return
     */
    public static boolean writeText(String filename, String data) {
        boolean flag = false;

        try {
            FileWriter fw = new FileWriter(filename);

            fw.write(data);
            flag = true;

            if (fw != null) {
                fw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return flag;
    }

    /**
     * 在文档后附加内容
     *
     * @param textName
     * @param date
     * @return
     */
    public static boolean appendText(String textPath, String textName, String date) {
        boolean flag = false;
        File filePath = new File(textPath);

        if (!filePath.exists()) {
            filePath.mkdirs();
        }

        try {
            FileWriter fw = new FileWriter(textPath + File.separator + textName, true);

            fw.append(date);
            flag = true;

            if (fw != null) {
                fw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return flag;
    }

    /**
     * 返回子目录，不返回文件
     *
     * @param path
     * @return
     */
    public static File[] listchildDirs(String path) {
        File f = new File(path);

        if (f.isFile() && f.isDirectory()) {
            File[] fs = f.listFiles(dirOnlyFilter);
            return fs;
        } else {
            return null;

        }

    }

    public static String getFilecharset(File sourceFile) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            //boolean checked = false;

            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(sourceFile));
            bis.mark(0);

            int read = bis.read(first3Bytes, 0, 3);
            System.out.println("字节大小：" + read);

            if (read == -1) {
                return charset; //文件编码为 ANSI
            } else if (first3Bytes[0] == (byte) 0xFF
                    && first3Bytes[1] == (byte) 0xFE) {

                charset = "UTF-16LE"; //文件编码为 Unicode
                //checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {

                charset = "UTF-16BE"; //文件编码为 Unicode big endian
                //checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {

                charset = "UTF-8"; //文件编码为 UTF-8
                //checked = true;
            }
            bis.reset();

            /*
             * if (!checked) { int loc = 0;
             *
             * while ((read = bis.read()) != -1) { loc++; if (read >= 0xF0)
             * break; if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
             * break; if (0xC0 <= read && read <= 0xDF) { read = bis.read(); if
             * (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF) // (0x80 // -
             * 0xBF),也可能在GB编码内 continue; else break; } else if (0xE0 <= read &&
             * read <= 0xEF) {// 也有可能出错，但是几率较小 read = bis.read(); if (0x80 <=
             * read && read <= 0xBF) { read = bis.read(); if (0x80 <= read &&
             * read <= 0xBF) { charset = "UTF-8"; break; } else break; } else
             * break; } } // System.out.println( loc + " " +
             * Integer.toHexString( read ) // );
   }
             */

            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return charset;
    }

    public static void main(String[] args) {
//        File f = new File("D:\\sijsdaafasf");
//        System.out.println(f.isDirectory());
//        System.out.println(f.isFile());
//        System.out.println(f.exists());
        File filePath = new File("d:/parse.csv");
        String charset = getFilecharset(filePath);
        
        System.out.println(charset);
    }
}
