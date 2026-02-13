package shared;

import java.awt.*;
import java.util.Map;
import javax.swing.*;

public final class UIInit {
    private UIInit() {}

    public static void initChineseUI() {
        Font font = pickChineseFont(16);
        setFontInDefaults(UIManager.getDefaults(), font);
        setFontInDefaults(UIManager.getLookAndFeelDefaults(), font);
    }

    private static Font pickChineseFont(int size) {
        String test = "中文";
        String[] candidates = {"Microsoft YaHei UI", "Microsoft YaHei", "SimHei", "SimSun", "Dialog"};
        for (String name : candidates) {
            Font f = new Font(name, Font.PLAIN, size);
            if (f.canDisplayUpTo(test) == -1) return f;
        }
        return new Font("Dialog", Font.PLAIN, size);
    }

    private static void setFontInDefaults(UIDefaults defaults, Font font) {
        for (Map.Entry<Object, Object> e : defaults.entrySet()) {
            Object key = e.getKey();
            Object value = e.getValue();
            if (key instanceof String && ((String) key).toLowerCase().endsWith(".font")) {
                defaults.put(key, font);
            } else if (value instanceof Font) {
                defaults.put(key, font);
            }
        }
    }
}
