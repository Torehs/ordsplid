package view;

import java.util.ArrayList;

import model.Letter;
import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import controller.WordController;

/**
 * Adapterclass for creating all the buttons used in the matrix during the game.
 */
public class ButtonAdapter extends BaseAdapter  {
	private Context mContext;
	private WordController wController = WordController.instance();
	private ArrayList<Letter> s = wController.retrieveNewLettersFromBag(16);
	private ArrayList<Button> buttonList = new ArrayList<Button>();
	
    // Declare button click listener variable
    private OnClickListener mOnButtonClick;

    // Method to set button click listener variable
    public void setOnButtonClickListener(OnClickListener listener) {
        mOnButtonClick = listener;
    }
	
	public ButtonAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return 16;
    }

    public Object getItem(int position) {
        return buttonList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

      
    // create a new ImageView for each item referenced by the Adapter
    @SuppressWarnings("null")
	public View getView(int position, View convertView, ViewGroup parent) {
        MatrixButton btn;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            btn = new MatrixButton(mContext);
            //btn.setLayoutParams(new GridView.LayoutParams(85, 85));
            btn.setPadding(8, 8, 8, 8);
            
            btn.setOnClickListener(mOnButtonClick);
        } else {
            btn = (MatrixButton) convertView;
        }
        buttonList.add(btn);
        //Get a random letter from the scrabblebag and set it to the button
        btn.setId(position);
        
        btn.setLetter(s.get(position));
       
        btn.setBackgroundColor(Color.LTGRAY);
        btn.setShadowLayer(9, 5, 5, Color.rgb(44, 44, 44));
          	
        return btn;
    }
}

/**
 * Button class with letters and animation
 */
class MatrixButton extends Button {
	private Letter letter;
	private int rotation = 0;
		
	public MatrixButton(Context context) {
		super(context);
		setDeselected();
	}
	

	public void setLetter(Letter letter) {
		this.letter = letter;
	
		String styledText = "<big><big> <font color='0x000000'>"
	            + toString() + "</font> </big></big>" 
	            + "<small><small><small <font color='0x000000'>" + getLetterPoints() + "</small></small></small>";
		
		setText(Html.fromHtml(styledText));
		
		Typeface font = Typeface.createFromAsset(getContext().getAssets(), "Zag Bold.otf");
		setTypeface(font);
	}
	
	public void setLetterPoints(Letter letter) {
		this.letter = letter;
		setText(getLetterPoints());
		
	}
	
	
	public String toString() {
		return letter.toString();
	}
	
	
	public String getLetterPoints() {
		return "" + letter.getPoints();
	}
	
	public Letter getLetter() {
		return letter;
	}
	
	public void setSelected() {
		setBackgroundColor(Color.BLUE);
	}
	
	public void setDeselected() {
		setBackgroundColor(Color.LTGRAY);
		setShadowLayer(9, 5, 5, Color.rgb(44, 44, 44));
	}
	
	public void setNewestSelected() {
		setBackgroundColor(Color.YELLOW);
		setShadowLayer(1, 1, 1, 1);
	}
	
	public void animateLetterSwitch(final Letter letter) {
		// Animate
		animate().rotationX((rotation += 360));
		animate().setDuration(300);
		animate().setStartDelay(100);
		// Set Letter
					
		//Used to time the letter change with rotation
		final Handler handler = new Handler();
		new Thread(new Runnable() {
	        public void run() {
	        	try {
					Thread.sleep(300);
				} catch (InterruptedException e) {}
	        	handler.post(new Runnable() {
					@Override
					public void run() {
						setLetter(letter);
					}
	        	});
	        }
	    }).start();
		
	}
	
	
}
