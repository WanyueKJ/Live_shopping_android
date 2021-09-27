package com.wanyue.common.utils;

import com.wanyue.common.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by  on 2018/7/11.
 */

public class FaceUtil {

    private static final Map<String, Integer> FACE_MAP;

    private static final List<String> FACE_LIST;

    static {
        FACE_MAP = new LinkedHashMap<>();
        FACE_MAP.put("[微笑]", R.mipmap.face_001);
        FACE_MAP.put("[色]", R.mipmap.face_002);
        FACE_MAP.put("[发呆]", R.mipmap.face_003);
        FACE_MAP.put("[抽烟]", R.mipmap.face_004);
        FACE_MAP.put("[抠鼻]", R.mipmap.face_005);
        FACE_MAP.put("[哭]", R.mipmap.face_006);
        FACE_MAP.put("[发怒]", R.mipmap.face_007);
        FACE_MAP.put("[呲牙]", R.mipmap.face_008);
        FACE_MAP.put("[睡]", R.mipmap.face_009);
        FACE_MAP.put("[害羞]", R.mipmap.face_010);
        FACE_MAP.put("[调皮]", R.mipmap.face_011);
        FACE_MAP.put("[晕]", R.mipmap.face_012);
        FACE_MAP.put("[衰]", R.mipmap.face_013);
        FACE_MAP.put("[闭嘴]", R.mipmap.face_014);
        FACE_MAP.put("[指点]", R.mipmap.face_015);
        FACE_MAP.put("[关注]", R.mipmap.face_016);
        FACE_MAP.put("[搞定]", R.mipmap.face_017);
        FACE_MAP.put("[胜利]", R.mipmap.face_018);
        FACE_MAP.put("[无奈]", R.mipmap.face_019);
        FACE_MAP.put("[打脸]", R.mipmap.face_020);
        FACE_MAP.put("[大笑]", R.mipmap.face_021);
        FACE_MAP.put("[哈欠]", R.mipmap.face_022);
        FACE_MAP.put("[害怕]", R.mipmap.face_023);
        FACE_MAP.put("[喜欢]", R.mipmap.face_024);
        FACE_MAP.put("[困]", R.mipmap.face_025);
        FACE_MAP.put("[疑问]", R.mipmap.face_026);
        FACE_MAP.put("[伤心]", R.mipmap.face_027);
        FACE_MAP.put("[鼓掌]", R.mipmap.face_028);
        FACE_MAP.put("[得意]", R.mipmap.face_029);
        FACE_MAP.put("[捂嘴]", R.mipmap.face_030);
        FACE_MAP.put("[惊恐]", R.mipmap.face_031);
        FACE_MAP.put("[思考]", R.mipmap.face_032);
        FACE_MAP.put("[吐血]", R.mipmap.face_033);
        FACE_MAP.put("[卖萌]", R.mipmap.face_034);
        FACE_MAP.put("[嘘]", R.mipmap.face_035);
        FACE_MAP.put("[生气]", R.mipmap.face_036);
        FACE_MAP.put("[尴尬]", R.mipmap.face_037);
        FACE_MAP.put("[笑哭]", R.mipmap.face_038);
        FACE_MAP.put("[口罩]", R.mipmap.face_039);
        FACE_MAP.put("[斜眼]", R.mipmap.face_040);
        FACE_MAP.put("[酷]", R.mipmap.face_041);
        FACE_MAP.put("[脸红]", R.mipmap.face_042);
        FACE_MAP.put("[大叫]", R.mipmap.face_043);
        FACE_MAP.put("[眼泪]", R.mipmap.face_044);
        FACE_MAP.put("[见钱]", R.mipmap.face_045);
        FACE_MAP.put("[嘟]", R.mipmap.face_046);
        FACE_MAP.put("[吓]", R.mipmap.face_047);
        FACE_MAP.put("[开心]", R.mipmap.face_048);
        FACE_MAP.put("[想哭]", R.mipmap.face_049);
        FACE_MAP.put("[郁闷]", R.mipmap.face_050);
        FACE_MAP.put("[互粉]", R.mipmap.face_051);
        FACE_MAP.put("[赞]", R.mipmap.face_052);
        FACE_MAP.put("[拜托]", R.mipmap.face_053);
        FACE_MAP.put("[唇]", R.mipmap.face_054);
        FACE_MAP.put("[粉]", R.mipmap.face_055);
        FACE_MAP.put("[666]", R.mipmap.face_056);
        FACE_MAP.put("[玫瑰]", R.mipmap.face_057);
        FACE_MAP.put("[黄瓜]", R.mipmap.face_058);
        FACE_MAP.put("[啤酒]", R.mipmap.face_059);
        FACE_MAP.put("[无语]", R.mipmap.face_060);
        FACE_MAP.put("[纠结]", R.mipmap.face_061);
        FACE_MAP.put("[吐舌]", R.mipmap.face_062);
        FACE_MAP.put("[差评]", R.mipmap.face_063);
        FACE_MAP.put("[飞吻]", R.mipmap.face_064);
        FACE_MAP.put("[再见]", R.mipmap.face_065);
        FACE_MAP.put("[拒绝]", R.mipmap.face_066);
        FACE_MAP.put("[耳机]", R.mipmap.face_067);
        FACE_MAP.put("[抱抱]", R.mipmap.face_068);
        FACE_MAP.put("[嘴]", R.mipmap.face_069);
        FACE_MAP.put("[露牙]", R.mipmap.face_070);
        FACE_MAP.put("[黄狗]", R.mipmap.face_071);
        FACE_MAP.put("[灰狗]", R.mipmap.face_072);
        FACE_MAP.put("[蓝狗]", R.mipmap.face_073);
        FACE_MAP.put("[狗]", R.mipmap.face_074);
        FACE_MAP.put("[脸黑]", R.mipmap.face_075);
        FACE_MAP.put("[吃瓜]", R.mipmap.face_076);
        FACE_MAP.put("[绿帽]", R.mipmap.face_077);
        FACE_MAP.put("[汗]", R.mipmap.face_078);
        FACE_MAP.put("[摸头]", R.mipmap.face_079);
        FACE_MAP.put("[阴险]", R.mipmap.face_080);
        FACE_MAP.put("[擦汗]", R.mipmap.face_081);
        FACE_MAP.put("[瞪眼]", R.mipmap.face_082);
        FACE_MAP.put("[疼]", R.mipmap.face_083);
        FACE_MAP.put("[鬼脸]", R.mipmap.face_084);
        FACE_MAP.put("[拇指]", R.mipmap.face_085);
        FACE_MAP.put("[亲]", R.mipmap.face_086);
        FACE_MAP.put("[大吐]", R.mipmap.face_087);
        FACE_MAP.put("[高兴]", R.mipmap.face_088);
        FACE_MAP.put("[敲打]", R.mipmap.face_089);
        FACE_MAP.put("[加油]", R.mipmap.face_090);
        FACE_MAP.put("[吐]", R.mipmap.face_091);
        FACE_MAP.put("[握手]", R.mipmap.face_092);
        FACE_MAP.put("[18禁]", R.mipmap.face_093);
        FACE_MAP.put("[菜刀]", R.mipmap.face_094);
        FACE_MAP.put("[威武]", R.mipmap.face_095);
        FACE_MAP.put("[给力]", R.mipmap.face_096);
        FACE_MAP.put("[爱心]", R.mipmap.face_097);
        FACE_MAP.put("[心碎]", R.mipmap.face_098);
        FACE_MAP.put("[便便]", R.mipmap.face_099);
        FACE_MAP.put("[礼物]", R.mipmap.face_100);
        FACE_MAP.put("[生日]", R.mipmap.face_101);
        FACE_MAP.put("[喝彩]", R.mipmap.face_102);
        FACE_MAP.put("[雷]", R.mipmap.face_103);

        FACE_LIST = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : FACE_MAP.entrySet()) {
            FACE_LIST.add(entry.getKey());
        }
    }

    public static List<String> getFaceList() {
        return FACE_LIST;
    }

    public static Integer getFaceImageRes(String key) {
        return FACE_MAP.get(key);
    }
}
