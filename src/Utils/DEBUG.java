package Utils;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.LinkedList;

public class DEBUG {

    private static DecimalFormat nf = new DecimalFormat("0.00");
    private static LinkedList<DebugMessage> debugList;
    private static int y = 0;

    public static void add(String intro, Object valueReference) {
        if (debugList == null) debugList = new LinkedList<>();
        boolean flag = false;

        for (DebugMessage m : debugList) {
            if (m.getIntro().equals(intro)) {
                m.setValueReference(valueReference);
                flag = true;
            }
        }

        if (!flag) {
            debugList.add(new DebugMessage(intro, valueReference));
        }
    }

    public static void render(Graphics2D g) {
        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.PLAIN, 10));
        y = 0;
        text("Debug log", g);

        if (debugList != null) {
            for (DebugMessage d : debugList) {
                text(d.getIntro() + d.getVarRef().toString(), g);
            }
        }
        g.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    private static void text(String debug, Graphics2D g) {
        y += g.getFontMetrics().getAscent();
        g.drawString(debug, 0, y);
    }

    private static class DebugMessage {
        private String intro;
        private Object varRef;

        public DebugMessage(String intro, Object varRef) {
            this.intro = intro;
            this.varRef = varRef;
        }

        public String getIntro() {
            return intro;
        }

        public Object getVarRef() {
            return varRef;
        }

        public void setValueReference(Object valueReference) {
            varRef = valueReference;
        }
    }
}
