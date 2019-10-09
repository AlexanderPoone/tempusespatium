package hk.edu.cuhk.cse.tempusespatium;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapLabel;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.font.FontAwesome;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * Created by softfeta on 2/26/18.
 */

// https://stackoverflow.com/questions/17956209/snowball-stemmer-usage

public class PuzzleRelevanceFragment extends Fragment implements PuzzleFragmentInterface {
    // term frequency (TF)
    // drag and drop three circles, nine tags, first find out four positives, then generate five negatives

    private boolean mFirst, isRevealed = false;
    private Map<String, List<String>> mRelevance;
    private int mColor;

    public PuzzleRelevanceFragment() {
    }

    public PuzzleRelevanceFragment(boolean first, Map<String, List<String>> relevance, int color) {
        mFirst = first;
        mRelevance = relevance;
        mColor = color;
    }

    @Override
    public boolean isRevealed() {
        return isRevealed;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_relevance, container, false);
        return view;
    }

    // Create a string for the ImageView label
    private static CharSequence[] TAGS = {"TAG0",
            "TAG1",
            "TAG2",
            "TAG3",
            "TAG4",
            "TAG5",
            "TAG6"};

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView[] textViews = {
                (TextView) view.findViewById(R.id.cat0),
                (TextView) view.findViewById(R.id.cat1),
                (TextView) view.findViewById(R.id.cat2)
        };
        String catArray[] = mRelevance.keySet().toArray(new String[mRelevance.size()]);
        for (int i = 0; i < textViews.length; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            if (catArray[i].length() > 30) {
                stringBuilder.append(catArray[i].substring(0, 14));
                if (catArray[i].charAt(14) != ' ' && catArray[i].charAt(15) != ' ')
                    stringBuilder.append('-');
                stringBuilder.append('\n');
                stringBuilder.append(catArray[i].substring(15, 29));
                if (catArray[i].charAt(29) != ' ' && catArray[i].charAt(30) != ' ')
                    stringBuilder.append('-');
                stringBuilder.append('\n');
                stringBuilder.append(catArray[i].substring(31));

                textViews[i].setText(stringBuilder.toString());
            } else if (catArray[i].length() > 15) {
                stringBuilder.append(catArray[i].substring(0, 14));
                if (catArray[i].charAt(14) != ' ' && catArray[i].charAt(15) != ' ')
                    stringBuilder.append('-');
                stringBuilder.append('\n');
                stringBuilder.append(catArray[i].substring(15));

                textViews[i].setText(stringBuilder.toString());
            } else {
                textViews[i].setText(catArray[i]);
            }
        }

        final BootstrapLabel[] labelViews = {
                (BootstrapLabel) view.findViewById(R.id.gum0),
                (BootstrapLabel) view.findViewById(R.id.gum1),
                (BootstrapLabel) view.findViewById(R.id.gum2),
                (BootstrapLabel) view.findViewById(R.id.gum3),
                (BootstrapLabel) view.findViewById(R.id.gum4),
                (BootstrapLabel) view.findViewById(R.id.gum5),
                (BootstrapLabel) view.findViewById(R.id.gum6)
        };

        List<String> merged = Lists.newArrayList(Iterables.concat(mRelevance.values()));

        for (int i = 0; i < labelViews.length; i++) {
            labelViews[i].setText(merged.get(i));
            labelViews[i].setTag(merged.get(i));  //labelViews[i].setTag(TAGS[i]);

// Sets a long click listener for the ImageView using an anonymous listener object that
// implements the OnLongClickListener interface
            final int finalI = i;

            labelViews[i].setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    // Create a new ClipData.
                    // This is done in two steps to provide clarity. The convenience method
                    // ClipData.newPlainText() can create a plain text ClipData in one step.

                    // Create a new ClipData.Item from the ImageView object's tag
                    ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());

                    // Create a new ClipData using the tag as a label, the plain text MIME type, and
                    // the already-created item. This will create a new ClipDescription object within the
                    // ClipData, and set its MIME type entry to "text/plain"
                    ClipData dragData = new ClipData((CharSequence) v.getTag(), new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);

                    // Instantiates the drag shadow builder.
                    View.DragShadowBuilder myShadow = new View.DragShadowBuilder(labelViews[finalI]);

                    // Starts the drag
                    v.startDrag(dragData,          // the data to be dragged
                            myShadow,              // the drag shadow builder
                            v,                     // local data about the drag and drop operation
                            0                 // flags (not currently used, set to 0)
                    );
                    return true;
                }
            });
        }

        View ellipse0 = (View) view.findViewById(R.id.ellipse0);
        View ellipse1 = (View) view.findViewById(R.id.ellipse1);
        View ellipse2 = (View) view.findViewById(R.id.ellipse2);
        ellipse0.setOnDragListener(new myDragEventListener(0));
        ellipse1.setOnDragListener(new myDragEventListener(1));
        ellipse2.setOnDragListener(new myDragEventListener(2));
    }

    @Override
    public int[] revealAnswer() {
        /* Do it here because drag listener only receives the MIME*/
        disableControls();
        int correctNum = 7;
        BootstrapLabel[] labelViews = {
                (BootstrapLabel) getView().findViewById(R.id.gum0),
                (BootstrapLabel) getView().findViewById(R.id.gum1),
                (BootstrapLabel) getView().findViewById(R.id.gum2),
                (BootstrapLabel) getView().findViewById(R.id.gum3),
                (BootstrapLabel) getView().findViewById(R.id.gum4),
                (BootstrapLabel) getView().findViewById(R.id.gum5),
                (BootstrapLabel) getView().findViewById(R.id.gum6)
        };
        int[] test = {
                R.id.box0, R.id.box0, R.id.box0, R.id.box1, R.id.box1, R.id.box2, R.id.box2
        };
        for (int i = 0; i < labelViews.length; i++) {
            BootstrapText.Builder builder = new BootstrapText.Builder(getContext());
            if (labelViews[i].getParent() != getView().findViewById(test[i])) {   //Change placeholder to map value
                correctNum--;
                labelViews[i].setBootstrapBrand(DefaultBootstrapBrand.DANGER);
                builder.addFontAwesomeIcon(FontAwesome.FA_CERTIFICATE);
            } else {
                labelViews[i].setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
                builder.addFontAwesomeIcon(FontAwesome.FA_CHECK);
            }
            builder.addText(" " + labelViews[i].getText());
            labelViews[i].setBootstrapText(builder.build());
        }
        if (mFirst) {
            return new int[]{(int) (correctNum - 3.5) * 2, 0};
        } else {
            return new int[]{0, (int) (correctNum - 3.5) * 2};
        }
    }

    @Override
    public void disableControls() {
        BootstrapLabel[] labelViews = {
                (BootstrapLabel) getView().findViewById(R.id.gum0),
                (BootstrapLabel) getView().findViewById(R.id.gum1),
                (BootstrapLabel) getView().findViewById(R.id.gum2),
                (BootstrapLabel) getView().findViewById(R.id.gum3),
                (BootstrapLabel) getView().findViewById(R.id.gum4),
                (BootstrapLabel) getView().findViewById(R.id.gum5),
                (BootstrapLabel) getView().findViewById(R.id.gum6)
        };
        for (BootstrapLabel label : labelViews) {
            label.setOnLongClickListener(null);
        }
    }


//    /* It creates a drag shadow for dragging a TextView as a small gray rectangle. */
//    private static class MyDragShadowBuilder extends View.DragShadowBuilder {
//        // The drag shadow image, defined as a drawable thing
//        private static Drawable shadow;
//
//        // Defines the constructor for myDragShadowBuilder
//        public MyDragShadowBuilder(View v) {
//            // Stores the View parameter passed to myDragShadowBuilder.
//            super(v);
//
//            // Creates a draggable image that will fill the Canvas provided by the system.
//            shadow = new ColorDrawable(Color.LTGRAY);
//        }
//
//        // Defines a callback that sends the drag shadow dimensions and touch point back to the
//        // system.
//        @Override
//        public void onProvideShadowMetrics(Point size, Point touch) {
//            // Defines local variables
//            int width, height;
//
//            // Sets the width of the shadow to half the width of the original View
//            width = getView().getWidth() / 2;
//
//            // Sets the height of the shadow to half the height of the original View
//            height = getView().getHeight() / 2;
//
//            // The drag shadow is a ColorDrawable. This sets its dimensions to be the same as the
//            // Canvas that the system will provide. As a result, the drag shadow will fill the
//            // Canvas.
//            shadow.setBounds(0, 0, width, height);
//
//            // Sets the size parameter's width and height values. These get back to the system
//            // through the size parameter.
//            size.set(width, height);
//
//            // Sets the touch point's position to be in the middle of the drag shadow
//            touch.set(width / 2, height / 2);
//        }
//
//        // Defines a callback that draws the drag shadow in a Canvas that the system constructs
//        // from the dimensions passed in onProvideShadowMetrics().
//        @Override
//        public void onDrawShadow(Canvas canvas) {
//            // Draws the ColorDrawable in the Canvas passed in from the system.
//            shadow.draw(canvas);
//        }
//    }

    /* */
    private class myDragEventListener implements View.OnDragListener {
        private int mCat;

        public myDragEventListener(int cat) {
            mCat = cat;
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {
            final int action = event.getAction();

            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // Determines if this View can accept the dragged data
                    if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        v.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN);
                        v.invalidate();
                        return true;
                    }
                    return false;

                case DragEvent.ACTION_DRAG_ENTERED:
                    v.getBackground().setColorFilter(getResources().getColor(R.color.Gamboge, null), PorterDuff.Mode.SRC_IN);
                    v.invalidate();
                    return true;

                case DragEvent.ACTION_DRAG_LOCATION:
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:
                    v.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN);
                    v.invalidate();
                    return true;

                case DragEvent.ACTION_DROP:
                    ClipData.Item item = event.getClipData().getItemAt(0);
                    String dragData = item.getText().toString();
                    Snackbar snack = Snackbar.make(getView().findViewById(R.id.fragment_outer), "Status: You have dragged " + dragData, Snackbar.LENGTH_SHORT);
                    if (mFirst) {
                        View view = snack.getView();
                        view.setRotation(180);
                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                        params.gravity = Gravity.TOP;
                        view.setLayoutParams(params);
                    }
                    snack.getView().setBackgroundColor(ContextCompat.getColor(getContext(), R.color.black_overlay));
                    snack.show();
                    v.getBackground().clearColorFilter();
                    v.invalidate();

                    View vw = (View) event.getLocalState();
                    vw.setForegroundGravity(Gravity.CENTER_HORIZONTAL);
                    ViewGroup owner = (ViewGroup) vw.getParent();
                    owner.removeView(vw);
                    LinearLayout container = null;
                    switch (mCat) {
                        case 0:
                            container = (LinearLayout) getView().findViewById(R.id.box0);
                            break;
                        case 1:
                            container = (LinearLayout) getView().findViewById(R.id.box1);
                            break;
                        case 2:
                            container = (LinearLayout) getView().findViewById(R.id.box2);
                            break;
                    }
                    container.addView(vw);
                    vw.setVisibility(View.VISIBLE);
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    v.getBackground().clearColorFilter();
                    v.invalidate();
//                    if (event.getResult()) {
//                        Toast.makeText(getContext(), "The drop was handled.", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(getContext(), "The drop didn't work.", Toast.LENGTH_SHORT).show();
//                    }
                    return true;

                default:
                    Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                    break;
            }
            return false;
        }
    }
}
