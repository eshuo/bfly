package com;

//import sun.misc.BASE64Encoder;
//import sun.misc.BASE64Decoder;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.Base64;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterOutputStream;

public class ZipUtil {


    /**
     * 压缩字符串
     *
     * @return
     */
    public static String gzipCompress(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            try (GZIPOutputStream gzip = new GZIPOutputStream(out)) {
                gzip.write(str.getBytes());
            }
            return Base64.getEncoder().encodeToString(out.toByteArray());
//            return new BASE64Encoder().encode(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * 解压字符串
     *
     * @return
     */
    public static String gzipUncompress(String str) {
        if (str == null) {
            return null;
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
//             ByteArrayInputStream in = new ByteArrayInputStream(new BASE64Decoder().decodeBuffer(str));
             ByteArrayInputStream in = new ByteArrayInputStream(Base64.getDecoder().decode(str));
             GZIPInputStream gzip = new GZIPInputStream(in)) {
            byte[] buffer = new byte[1024];
            int offset;
            while ((offset = gzip.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            return out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * 压缩字符串,默认梳utf-8
     *
     * @param text
     * @return
     */
    public static String base64Zip(String text) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(out)) {
                deflaterOutputStream.write(text.getBytes("UTF-8"));
            }
            return new BASE64Encoder().encode(out.toByteArray());
        } catch (IOException e) {
        }
        return "";
    }


    /**
     * 解压字符串,默认utf-8
     *
     * @param text
     * @return
     */
    public static String base64Unzip(String text) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            try (OutputStream outputStream = new InflaterOutputStream(os)) {
                outputStream.write(new BASE64Decoder().decodeBuffer(text));
            }
            return new String(os.toByteArray(), "UTF-8");
        } catch (IOException e) {
        }
        return "";
    }


    public static String compress(String data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length());
        GZIPOutputStream gzip = new GZIPOutputStream(bos);
        gzip.write(data.getBytes());
        gzip.close();
        byte[] compressed = bos.toByteArray();
        bos.close();
        return new BASE64Encoder().encode(compressed);
    }

    public static String decompress(String compressed) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(new BASE64Decoder().decodeBuffer(compressed));
        GZIPInputStream gis = new GZIPInputStream(bis);
        BufferedReader br = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        gis.close();
        bis.close();
        return sb.toString();
    }


    // 测试方法
    public static void main(String[] args)  {

        String json = "{\"type\":\"Container\",\"height\":880.0,\"decoration\":{\"color\":\"#ffffff\"},\"child\":{\"shrinkWrap\":true,\"type\":\"ListView\",\"children\":[{\"type\":\"Container\",\"child\":{\"mainAxisAlignment\":\"start\",\"crossAxisAlignment\":\"start\",\"type\":\"Row\",\"children\":[{\"type\":\"Expanded\",\"flex\":80.0,\"child\":{\"type\":\"Container\",\"height\":50.0,\"color\":\"#ffffff\",\"margin\":\"10,0,0,20\",\"padding\":\"0,0,0,0\",\"click_event\":\"route://productDetail?goods_id=123\",\"decoration\":{\"border\":{\"color\":\"#cab8b8\"},\"color\":\"#ffffff\",\"shape\":\"rectangle\",\"borderRadius\":\"5\"},\"dataProp\":{\"id\":\"ASPAGEAS202203141723443227000002\",\"fieldMapping\":{},\"autoUpdate\":false,\"updateInterval\":0,\"checkRuleList\":[],\"url\":\"\",\"inputFieldMapping\":{}},\"interactiveProp\":{\"id\":\"ASPAGEAS202203141723443227000002\",\"input\":\"\",\"output\":\"\",\"enableInteraction\":false,\"interactionStyles\":[],\"enableScroll\":false,\"scrollStyles\":[]},\"child\":{\"mainAxisAlignment\":\"center\",\"crossAxisAlignment\":\"center\",\"mainAxisSize\":\"max\",\"textBaseline\":\"alphabetic\",\"textDirection\":\"ltr\",\"verticalDirection\":\"down\",\"type\":\"Row\",\"children\":[{\"type\":\"Expanded\",\"child\":{\"type\":\"Text\",\"data\":\"搜索\"}}]}}},{\"type\":\"Expanded\",\"flex\":15.0,\"child\":{\"alignment\":\"center\",\"type\":\"Container\",\"height\":50.0,\"margin\":\"0,0,0,20\",\"padding\":\"8,4,0,0\",\"child\":{\"type\":\"NetworkImage\",\"src\":\"OSOSSFOS202203141127441911000002\",\"dataProp\":{\"id\":\"ASPAGEAS202203141723443227000003\",\"fieldMapping\":{},\"autoUpdate\":false,\"updateInterval\":0,\"checkRuleList\":[],\"url\":\"\",\"inputFieldMapping\":{}},\"interactiveProp\":{\"id\":\"ASPAGEAS202203141723443227000003\",\"input\":\"\",\"output\":\"\",\"enableInteraction\":false,\"interactionStyles\":[],\"enableScroll\":false,\"scrollStyles\":[]}}}}]}},{\"type\":\"Container\",\"child\":{\"mainAxisAlignment\":\"start\",\"crossAxisAlignment\":\"start\",\"type\":\"Row\",\"children\":[{\"type\":\"Expanded\",\"flex\":69.0,\"child\":{\"type\":\"Container\",\"height\":125.0,\"margin\":\"10,0,0,0\",\"padding\":\"0,0,0,0\",\"decoration\":{\"borderRadius\":\"20\"},\"child\":{\"scrollDirection\":\"horizontal\",\"type\":\"ListView\",\"flex\":69.0,\"margin\":\"10,0,0,0\",\"padding\":\"0,0,0,0\",\"dataProp\":{\"id\":\"ASPAGEAS202203141723443227000004\",\"fieldMapping\":{},\"autoUpdate\":false,\"updateInterval\":0,\"checkRuleList\":[],\"url\":\"/api/v1.0/user/getbanner\",\"inputFieldMapping\":{}},\"children\":[{\"type\":\"Container\",\"width\":60.0,\"margin\":\"0,10,20,0\",\"interactiveProp\":{\"id\":\"ASPAGEAS202203141723443227000004\",\"input\":\"id\",\"output\":\"id\",\"enableInteraction\":false,\"interactionStyles\":[],\"enableScroll\":false,\"scrollStyles\":[]},\"child\":{\"mainAxisAlignment\":\"start\",\"crossAxisAlignment\":\"start\",\"type\":\"Column\",\"children\":[{\"alignment\":\"center\",\"type\":\"Container\",\"child\":{\"type\":\"Container\",\"width\":60.0,\"height\":60.0,\"margin\":\"0,0,0,10\",\"child\":{\"borderRadius\":\"6\",\"type\":\"ClipRRect\",\"child\":{\"fit\":\"fill\",\"type\":\"NetworkImage\",\"src\":\"${img}\"}}}},{\"alignment\":\"center\",\"type\":\"Container\",\"width\":60.0,\"height\":20.0,\"margin\":\"0,0,0,0\",\"child\":{\"type\":\"Text\",\"data\":\"${value}\",\"style\":{\"color\":\"#000000\",\"fontSize\":12.0}}}]}}]}}},{\"type\":\"Expanded\",\"flex\":23.0,\"child\":{\"type\":\"Container\",\"height\":125.0,\"margin\":\"0,0,10,0\",\"padding\":\"0,0,0,0\",\"decoration\":{\"borderRadius\":\"0\"},\"child\":{\"scrollDirection\":\"horizontal\",\"type\":\"ListView\",\"flex\":23.0,\"margin\":\"0,0,10,0\",\"padding\":\"0,0,0,0\",\"dataProp\":{\"id\":\"ASPAGEAS202203141723443227000007\",\"fieldMapping\":{},\"autoUpdate\":false,\"updateInterval\":0,\"checkRuleList\":[],\"url\":\"\",\"inputFieldMapping\":{}},\"children\":[{\"type\":\"Container\",\"width\":60.0,\"margin\":\"0,10,0,0\",\"interactiveProp\":{\"id\":\"ASPAGEAS202203141723443227000007\",\"input\":\"\",\"output\":\"\",\"enableInteraction\":false,\"interactionStyles\":[],\"enableScroll\":false,\"scrollStyles\":[]},\"child\":{\"mainAxisAlignment\":\"start\",\"crossAxisAlignment\":\"start\",\"type\":\"Column\",\"children\":[{\"alignment\":\"center\",\"type\":\"Container\",\"child\":{\"type\":\"Container\",\"width\":60.0,\"height\":60.0,\"margin\":\"0,0,0,10\",\"child\":{\"borderRadius\":\"6\",\"type\":\"ClipRRect\",\"child\":{\"fit\":\"fill\",\"type\":\"NetworkImage\",\"src\":\"https://laiyue-static.oss-cn-qingdao.aliyuncs.com/png/icon_home_more.png\"}}}},{\"alignment\":\"center\",\"type\":\"Container\",\"width\":60.0,\"height\":20.0,\"margin\":\"0,0,0,10\",\"child\":{\"type\":\"Text\",\"data\":\"更多\",\"style\":{\"color\":\"#000000\",\"fontSize\":12.0}}}]}}]}}}]}},{\"type\":\"Container\",\"child\":{\"type\":\"Container\",\"height\":100.0,\"margin\":\"10,0,10,0\",\"padding\":\"0,0,0,0\",\"child\":{\"autoplay\":true,\"type\":\"Swiper\",\"dataProp\":{\"id\":\"ASPAGEAS202203141723443227000010\",\"fieldMapping\":{},\"autoUpdate\":false,\"updateInterval\":0,\"checkRuleList\":[],\"url\":\"/api/v1.0/bannerconfig/list\",\"inputFieldMapping\":{\"category\":\"APP_INDEX\"}},\"interactiveProp\":{\"id\":\"ASPAGEAS202203141723443227000010\",\"input\":\"\",\"output\":\"\",\"enableInteraction\":false,\"interactionStyles\":[],\"enableScroll\":false,\"scrollStyles\":[]},\"children\":[{\"borderRadius\":\"10\",\"type\":\"ClipRRect\",\"child\":{\"fit\":\"fill\",\"type\":\"NetworkImage\",\"src\":\"\"}}]}}},{\"type\":\"Container\",\"child\":{\"mainAxisAlignment\":\"start\",\"crossAxisAlignment\":\"start\",\"type\":\"Container\",\"height\":100.0,\"margin\":\"0,10,0,20\",\"padding\":\"0,0,0,0\",\"child\":{\"shrinkWrap\":true,\"type\":\"ListView\",\"dataProp\":{\"id\":\"ASPAGEAS202203141723443227000012\",\"fieldMapping\":{},\"autoUpdate\":false,\"updateInterval\":0,\"checkRuleList\":[],\"url\":\"/api/v1.0/dynamic/topic/hot/list\",\"inputFieldMapping\":{\"page.size\":\"3\",\"page.currentPage\":\"1\"}},\"elementElementConfig\":{},\"children\":[{\"type\":\"Container\",\"height\":66.0,\"margin\":\"0,0,0,0\",\"padding\":\"0,0,0,0\",\"interactiveProp\":{\"id\":\"ASPAGEAS202203141723443227000012\",\"input\":\"\",\"output\":\"\",\"enableInteraction\":false,\"interactionStyles\":[],\"enableScroll\":false,\"scrollStyles\":[]},\"child\":{\"mainAxisAlignment\":\"start\",\"crossAxisAlignment\":\"start\",\"type\":\"Row\",\"children\":[{\"type\":\"Expanded\",\"flex\":35.0,\"child\":{\"type\":\"Container\",\"height\":66.0,\"child\":{\"borderRadius\":\"6\",\"type\":\"ClipRRect\",\"child\":{\"fit\":\"fill\",\"type\":\"NetworkImage\",\"src\":\"https://laiyue-static.oss-cn-qingdao.aliyuncs.com/flutter/png/hopic/${image}$.png\"}}}},{\"type\":\"Expanded\",\"flex\":65.0,\"child\":{\"type\":\"Container\",\"child\":{\"shrinkWrap\":true,\"type\":\"ListView\",\"children\":[{\"type\":\"Container\",\"child\":{\"type\":\"Container\",\"height\":20.0,\"child\":{\"type\":\"Text\",\"data\":\"$title$\",\"style\":{\"color\":\"#000000\",\"fontSize\":12.0}}}},{\"type\":\"Container\",\"child\":{\"type\":\"Container\",\"height\":20.0,\"child\":{\"type\":\"Text\",\"data\":\"$viewsNum$\",\"style\":{\"color\":\"#000000\",\"fontSize\":12.0}}}},{\"type\":\"Container\",\"child\":{\"type\":\"Spacer\"}}]}}}]}}]}}},{\"type\":\"Container\",\"child\":{\"mainAxisAlignment\":\"start\",\"crossAxisAlignment\":\"start\",\"type\":\"Row\",\"children\":[{\"type\":\"Expanded\",\"flex\":40.0,\"child\":{\"type\":\"Container\",\"height\":120.0,\"margin\":\"10,0,0,0\",\"padding\":\"0,0,0,0\",\"child\":{\"borderRadius\":\"5\",\"type\":\"ClipRRect\",\"child\":{\"fit\":\"fill\",\"type\":\"NetworkImage\",\"src\":\"OSOSSFOS202203142015441911000001\",\"dataProp\":{\"id\":\"ASPAGEAS202203141723443227000018\",\"fieldMapping\":{},\"autoUpdate\":false,\"updateInterval\":0,\"checkRuleList\":[],\"url\":\"\",\"inputFieldMapping\":{}},\"interactiveProp\":{\"id\":\"ASPAGEAS202203141723443227000018\",\"input\":\"\",\"output\":\"\",\"enableInteraction\":false,\"interactionStyles\":[],\"enableScroll\":false,\"scrollStyles\":[]}}}}},{\"type\":\"Expanded\",\"flex\":40.0,\"child\":{\"type\":\"Container\",\"height\":120.0,\"margin\":\"10,0,0,0\",\"padding\":\"0,0,0,0\",\"child\":{\"borderRadius\":\"5\",\"type\":\"ClipRRect\",\"child\":{\"fit\":\"fill\",\"type\":\"NetworkImage\",\"src\":\"OSOSSFOS202203142015441911000002\",\"dataProp\":{\"id\":\"ASPAGEAS202203141723443227000019\",\"fieldMapping\":{},\"autoUpdate\":false,\"updateInterval\":0,\"checkRuleList\":[],\"url\":\"\",\"inputFieldMapping\":{}},\"interactiveProp\":{\"id\":\"ASPAGEAS202203141723443227000019\",\"input\":\"\",\"output\":\"\",\"enableInteraction\":false,\"interactionStyles\":[],\"enableScroll\":false,\"scrollStyles\":[]}}}}}]}},{\"type\":\"Container\",\"child\":{\"mainAxisAlignment\":\"start\",\"crossAxisAlignment\":\"start\",\"type\":\"Row\",\"children\":[{\"type\":\"Expanded\",\"flex\":80.0,\"child\":{\"alignment\":\"centerLeft\",\"type\":\"Container\",\"height\":30.0,\"margin\":\"0,0,0,0\",\"padding\":\"0,5,0,0\",\"child\":{\"type\":\"Text\",\"data\":\"礼品商城\",\"dataProp\":{\"id\":\"ASPAGEAS202203141723443227000020\",\"fieldMapping\":{},\"autoUpdate\":false,\"updateInterval\":0,\"checkRuleList\":[],\"url\":\"\",\"inputFieldMapping\":{}},\"interactiveProp\":{\"id\":\"ASPAGEAS202203141723443227000020\",\"input\":\"\",\"output\":\"\",\"enableInteraction\":false,\"interactionStyles\":[],\"enableScroll\":false,\"scrollStyles\":[]},\"style\":{\"color\":\"#000000\",\"fontSize\":16.0}}}},{\"type\":\"Expanded\",\"flex\":20.0,\"child\":{\"type\":\"Container\",\"margin\":\"0,0,0,0\",\"padding\":\"0,5,0,0\",\"child\":{\"type\":\"SizedBox\",\"height\":30.0,\"child\":{\"type\":\"RaisedButton\",\"color\":\"#ffffff\",\"margin\":\"0,0,0,0\",\"padding\":\"0,5,0,0\",\"shape\":{\"side\":{\"color\":\"#bdc2d2\",\"width\":1.0,\"style\":1},\"borderRadius\":\"5\"},\"child\":{\"type\":\"Text\",\"data\":\"更多>\",\"dataProp\":{\"id\":\"ASPAGEAS202203141723443227000021\",\"fieldMapping\":{},\"autoUpdate\":false,\"updateInterval\":0,\"checkRuleList\":[],\"url\":\"\",\"inputFieldMapping\":{}},\"interactiveProp\":{\"id\":\"ASPAGEAS202203141723443227000021\",\"input\":\"\",\"output\":\"\",\"enableInteraction\":true,\"interactionStyles\":[{\"id\":\"ASINPSAS202203141723443227000001\",\"interactionId\":\"ASPAGEAS202203141723443227000021\",\"interactionType\":\"PAGE_TARGET\",\"interactionTargetId\":\" mall\",\"newTemplate\":true,\"interactionTargetType\":\"nativePage\"}],\"enableScroll\":false,\"scrollStyles\":[]},\"style\":{\"color\":\"#000000\",\"fontSize\":14.0}}}}}}]}},{\"type\":\"Container\",\"child\":{\"type\":\"Container\",\"height\":160.0,\"margin\":\"0,0,0,0\",\"padding\":\"0,0,0,0\",\"decoration\":{\"borderRadius\":\"0\"},\"child\":{\"scrollDirection\":\"horizontal\",\"type\":\"ListView\",\"flex\":100.0,\"margin\":\"0,0,0,0\",\"padding\":\"0,0,0,0\",\"dataProp\":{\"id\":\"ASPAGEAS202203141723443227000022\",\"fieldMapping\":{},\"autoUpdate\":false,\"updateInterval\":0,\"checkRuleList\":[],\"url\":\"/api/v1.0/goods/list\",\"inputFieldMapping\":{\"id\":\"hot\"}},\"children\":[{\"type\":\"Container\",\"width\":76.0,\"margin\":\"0,0,20,0\",\"interactiveProp\":{\"id\":\"ASPAGEAS202203141723443227000022\",\"input\":\"id\",\"output\":\"id\",\"enableInteraction\":true,\"interactionStyles\":[{\"id\":\"ASINPSAS202203141723443227000002\",\"interactionId\":\"ASPAGEAS202203141723443227000022\",\"interactionType\":\"PAGE_TARGET\",\"interactionTargetId\":\"goodsInfo\",\"newTemplate\":true,\"interactionTargetType\":\"nativePage\"}],\"enableScroll\":false,\"scrollStyles\":[]},\"child\":{\"mainAxisAlignment\":\"start\",\"crossAxisAlignment\":\"start\",\"type\":\"Column\",\"children\":[{\"alignment\":\"center\",\"type\":\"Container\",\"child\":{\"type\":\"Container\",\"width\":76.0,\"height\":76.0,\"margin\":\"0,0,0,10\",\"child\":{\"borderRadius\":\"6\",\"type\":\"ClipRRect\",\"child\":{\"fit\":\"fill\",\"type\":\"NetworkImage\",\"src\":\"$img$\"}}}},{\"alignment\":\"centerLeft\",\"type\":\"Container\",\"width\":76.0,\"height\":20.0,\"margin\":\"0,0,0,10\",\"child\":{\"type\":\"Text\",\"data\":\"$value$\",\"style\":{\"color\":\"#000000\",\"fontSize\":12.0}}}]}}]}}},{\"type\":\"Container\",\"child\":{\"mainAxisAlignment\":\"start\",\"crossAxisAlignment\":\"start\",\"type\":\"Row\",\"children\":[{\"type\":\"Expanded\",\"flex\":80.0,\"child\":{\"alignment\":\"centerLeft\",\"type\":\"Container\",\"height\":30.0,\"margin\":\"0,0,0,0\",\"padding\":\"0,5,0,0\",\"child\":{\"type\":\"Text\",\"data\":\"新闻资讯\",\"dataProp\":{\"id\":\"ASPAGEAS202203141723443227000025\",\"fieldMapping\":{},\"autoUpdate\":false,\"updateInterval\":0,\"checkRuleList\":[],\"url\":\"\",\"inputFieldMapping\":{}},\"interactiveProp\":{\"id\":\"ASPAGEAS202203141723443227000025\",\"input\":\"\",\"output\":\"\",\"enableInteraction\":false,\"interactionStyles\":[],\"enableScroll\":false,\"scrollStyles\":[]},\"style\":{\"color\":\"#000000\",\"fontSize\":16.0}}}},{\"type\":\"Expanded\",\"flex\":20.0,\"child\":{\"type\":\"Container\",\"margin\":\"0,0,0,0\",\"padding\":\"0,5,0,0\",\"child\":{\"type\":\"SizedBox\",\"height\":30.0,\"child\":{\"type\":\"RaisedButton\",\"color\":\"#ffffff\",\"margin\":\"0,0,0,0\",\"padding\":\"0,5,0,0\",\"shape\":{\"side\":{\"color\":\"#bdc2d2\",\"width\":1.0,\"style\":1},\"borderRadius\":\"5\"},\"child\":{\"type\":\"Text\",\"data\":\"更多>\",\"dataProp\":{\"id\":\"ASPAGEAS202203141723443227000026\",\"fieldMapping\":{},\"autoUpdate\":false,\"updateInterval\":0,\"checkRuleList\":[],\"url\":\"\",\"inputFieldMapping\":{}},\"interactiveProp\":{\"id\":\"ASPAGEAS202203141723443227000026\",\"input\":\"\",\"output\":\"\",\"enableInteraction\":true,\"interactionStyles\":[{\"id\":\"ASINPSAS202203141723443227000003\",\"interactionId\":\"ASPAGEAS202203141723443227000026\",\"interactionType\":\"PAGE_TARGET\",\"interactionTargetId\":\"ASDYNPAS202203122124244324000001\",\"newTemplate\":true,\"interactionTargetType\":\"page\"}],\"enableScroll\":false,\"scrollStyles\":[]},\"style\":{\"color\":\"#000000\",\"fontSize\":14.0}}}}}}]}},{\"type\":\"Container\",\"child\":{\"mainAxisAlignment\":\"start\",\"crossAxisAlignment\":\"start\",\"type\":\"Container\",\"height\":400.0,\"margin\":\"0,5,0,20\",\"padding\":\"0,0,0,0\",\"child\":{\"shrinkWrap\":true,\"type\":\"ListView\",\"dataProp\":{\"id\":\"ASPAGEAS202203141723443227000027\",\"fieldMapping\":{\"image\":\"data[i].thumbnail\",\"subtitle\":\"data[i].subtitle\",\"time\":\"data[i].publishTimeStr\",\"title\":\"data[i].title\",\"viewsNum\":\"data[i].viewsNum\"},\"autoUpdate\":false,\"updateInterval\":0,\"checkRuleList\":[],\"url\":\"/api/v1.0/app/article/hot\",\"inputFieldMapping\":{}},\"elementElementConfig\":{},\"children\":[{\"type\":\"Container\",\"height\":100.0,\"margin\":\"0,10,10,10\",\"padding\":\"0,0,0,0\",\"interactiveProp\":{\"id\":\"ASPAGEAS202203141723443227000027\",\"input\":\"\",\"output\":\"\",\"enableInteraction\":true,\"interactionStyles\":[{\"id\":\"ASINPSAS202203141723443227000004\",\"interactionId\":\"ASPAGEAS202203141723443227000027\",\"interactionType\":\"PAGE_TARGET\",\"interactionTargetId\":\"article\",\"newTemplate\":true,\"interactionTargetType\":\"nativePage\"},{\"id\":\"ASINPSAS202203141723443227000005\",\"interactionId\":\"ASPAGEAS202203141723443227000027\",\"interactionType\":\"VARIABLE\",\"originVariable\":\"id\",\"targetVariable\":\"${this.id}\"}],\"enableScroll\":false,\"scrollStyles\":[]},\"child\":{\"mainAxisAlignment\":\"start\",\"crossAxisAlignment\":\"start\",\"type\":\"Row\",\"children\":[{\"type\":\"Expanded\",\"flex\":65.0,\"child\":{\"type\":\"Container\",\"child\":{\"shrinkWrap\":true,\"type\":\"ListView\",\"children\":[{\"type\":\"Container\",\"child\":{\"type\":\"Container\",\"height\":26.0,\"child\":{\"type\":\"Text\",\"data\":\"${title}\",\"style\":{\"color\":\"#000000\",\"fontSize\":18.0}}}},{\"type\":\"Container\",\"child\":{\"type\":\"Spacer\"}},{\"type\":\"Container\",\"child\":{\"mainAxisAlignment\":\"start\",\"crossAxisAlignment\":\"start\",\"type\":\"Row\",\"children\":[{\"type\":\"Expanded\",\"flex\":10.0,\"child\":{\"type\":\"Container\",\"height\":20.0,\"child\":{\"fit\":\"fill\",\"type\":\"NetworkImage\",\"src\":\"https://laiyue-static.oss-cn-qingdao.aliyuncs.com/flutter/hot_discussion.png\"}}},{\"type\":\"Expanded\",\"flex\":40.0,\"child\":{\"type\":\"Container\",\"height\":22.0,\"child\":{\"type\":\"Text\",\"data\":\"${time}\",\"style\":{\"color\":\"#000000\",\"fontSize\":14.0}}}},{\"type\":\"Expanded\",\"flex\":35.0,\"child\":{\"type\":\"Container\",\"height\":22.0,\"child\":{\"type\":\"Text\",\"data\":\"${viewsNum}阅读\",\"style\":{\"color\":\"#000000\",\"fontSize\":14.0}}}}]}}]}}},{\"type\":\"Expanded\",\"flex\":35.0,\"child\":{\"type\":\"Container\",\"height\":100.0,\"child\":{\"borderRadius\":\"6\",\"type\":\"ClipRRect\",\"child\":{\"fit\":\"fill\",\"type\":\"NetworkImage\",\"src\":\"\"}}}}]}}]}}}]}}";

        System.err.println("size---------" + json.length());
        final String compress = ZipUtil.gzipCompress(json);

        System.err.println("compress size---------" + compress.length());

        System.out.println(compress);

        final String uncompress = ZipUtil.gzipUncompress(compress);

        System.err.println("uncompress size---------" + uncompress.length());

        System.out.println(uncompress);


    }

}