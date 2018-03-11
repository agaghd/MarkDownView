package io.github.agaghd.markdownview.utils;

import android.text.Layout;
import android.text.Spannable;
import android.text.method.BaseMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * author : wjy
 * time   : 2018/03/11
 * desc   : 替代LinkMovementMethod受用
 * 由于LinkMovementMethod继承自ScrollingMovementMethod，点击时内容会滑动，所以用自定义类型替代它
 */

public class ClickOnlyMovementMethod extends BaseMovementMethod {

    private static ClickOnlyMovementMethod clickOnlyMovementMethod;

    public static ClickOnlyMovementMethod getInstance() {
        if (clickOnlyMovementMethod == null) {
            synchronized (ClickOnlyMovementMethod.class) {
                if (clickOnlyMovementMethod == null) {
                    clickOnlyMovementMethod = new ClickOnlyMovementMethod();
                }
            }
        }
        return clickOnlyMovementMethod;
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);

            if (link.length != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    link[0].onClick(widget);
                    // Add this line of code for removing the selection effect
                    // when your finger moves away
//                        Selection.removeSelection(buffer);
                }
                return true;
            }
        }
        return true;
    }

    private ClickOnlyMovementMethod() {

    }
}
