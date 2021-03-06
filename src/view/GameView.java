package view;

import java.util.ArrayList;

import model.Word;
import tdt4240.ordsplid.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import controller.GameController;
import controller.SettingsController;
import controller.Tools;
import controller.WordController;

/**
 * Class for the view of the game. Showing the current running game.
 */
public class GameView extends Activity{
	private ButtonAdapter btnAdapter;
	private String s = "";
	private WordController wController = WordController.instance();
	private Word currentWord = new Word();
	private MatrixButton prevClickedBtn;
	private ArrayList<MatrixButton> activeButtons;
	
	private Button ok;	
    private TextView text;
    private TextView scoreView;
    private TextView playerNameView;
    private TextView timerView;
    
    private CountDownTimer timer =  new CountDownTimer(SettingsController.instance().getTurnTime() * 1000, 1000) {
		public void onTick(long millisUntilFinished) {
			GameView.this.setTimer("" + millisUntilFinished / 1000);
		}
		
		public void onFinish() {
			GameView.this.setTimer("0");
			GameController.instance().endTurn();
		}
	};
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    GameController.instance().setOngoingGame(true);
	    GameController.instance().setGameView(this);

    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    	
	    setContentView(R.layout.activity_game_view);

	    activeButtons = new ArrayList<MatrixButton>();
	    btnAdapter = new ButtonAdapter(this);
	    text = (TextView) findViewById(R.id.wordView);
	    scoreView = (TextView) findViewById(R.id.scoreView);
	    timerView = (TextView) findViewById(R.id.timerView);
	    playerNameView = (TextView) findViewById(R.id.playerNameView);
	    
	    //Setting up the OK button
	    ok = (Button)findViewById(R.id.game_ok_button);
	    
	    updateOKButton();
	  
	    //Setting the GridView and adapter
	    GridView gridview = (GridView) findViewById(R.id.gridView);
	    gridview.setAdapter(btnAdapter);

	    //ClickListener for the buttons in the matrix
	    btnAdapter.setOnButtonClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MatrixButton temp = (MatrixButton) v;
				
				if (activeButtons.size() > 0 && activeButtons.get(activeButtons.size() - 1) == temp) {
					currentWord.remove(currentWord.size() - 1);
					activeButtons.remove(temp);
					s = s.substring(0, s.length() - 1);
					text.setText(s);
					temp.setDeselected();
					if (activeButtons.size() > 0) {
						prevClickedBtn = activeButtons.get(activeButtons.size() - 1);
						prevClickedBtn.setNewestSelected();
					}
					
				}
				else if (activeButtons.contains(temp)) {
					return;
				}
				else {
					s += temp.toString();
					currentWord.add(temp.getLetter());
					activeButtons.add(temp);
					text.setText(s);
					temp.setNewestSelected();
					
					if(s.length() > 1){
						prevClickedBtn.setSelected();
					}
					prevClickedBtn = temp;
				}
				updateOKButton();
			}
			
			
		});
	    
	    //ClickListener for the OK button
	    ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Check if the word is allowed
				if(wController.checkWord(currentWord)){
					GameController.instance().submitWord(currentWord);
					//text.setText("word exists");
					
					//s = "";
				}else{
					//text.setText("wrong word");
					//s = "";
				}
				clearScreen();
			}
		});
	    GameController.instance().updateInfoBarInGameView();
	    timer.start();
	}
	
	private void updateOKButton() {
		if(!wController.checkWord(currentWord)){
			ok.setBackgroundColor(Color.RED);
			ok.setText("Erase word");
		}else{
			ok.setBackgroundColor(Color.GREEN);
			ok.setText("Submit");
		}
	}
	
	public void displayToast(String text) {
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		
		Toast toast = Toast.makeText(context, text, duration);
		
		
		Point p = Tools.getScreenDimensions(this);
		int offset = (int) (p.y * 0.15);
		toast.setGravity(Gravity.TOP, 0, offset);
		toast.show();
	}
	
	public void switchLetters(Word newLetters) {
		if (activeButtons.size() != newLetters.size()){
			displayToast("ERROR! Sizes does not match\n" + activeButtons.size() + " - " + newLetters.size());
			return;
		}
		for (int i = 0; i < activeButtons.size(); i++) {
			activeButtons.get(i).animateLetterSwitch(newLetters.get(i));
		}
	}
	
	public void clearScreen() {
		for (MatrixButton button : activeButtons) {
			button.setDeselected();
			
		}
		activeButtons = new ArrayList<MatrixButton>();
		currentWord = new Word();
		s = "";
		text.setText(s);
		updateOKButton();
	}
	
	
	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Current game will be deleted").setTitle("Are you sure you want to exit?");
		
		builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   //GameController.instance().nextPlayer();
	        	   GameController.instance().setOngoingGame(false);
	        	   timer.cancel();
	        	   GameView.super.onBackPressed();
	           }
	       });
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               dialog.dismiss();
	           }
	       });
		AlertDialog dialog = builder.create();
		
		dialog.show();
	}
	
	public void setScore(String score) {
		scoreView.setText(score);
	}
	
	public void setTimer(String time) {
		timerView.setText(time);
	}
	
	public void setPlayerName(String playerName) {
		playerNameView.setText(playerName);
	}
}
