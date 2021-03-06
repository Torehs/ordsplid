package model;

import java.util.ArrayList;

/**
 * The bag containing all the current available letters.
 * 
 * It is up to the user to make sure that all letters removed are also returned at some point.
 * If not the bag might run out of letters, and the probabilities will no longer work correctly.
 *
 *
 */
public class ScrabbleBag {
	private Alphabet list;
	private ArrayList<Letter> bag = new ArrayList<Letter>();
	
	
	public ScrabbleBag() {
		list = Alphabet.instance();
		fillBag();
	}
	
	
	public void fillBag() {
		for (int i = 0; i < Constants.NUMBER_OF_DISTINCT_LETTERS; i++) {
			for (int k = 0; k < list.get(i).getTotalPieces(); k++) {
				bag.add(list.get(i));
			}
		}
	}
	
	/**
	 * Removes a random letter from bag
	 * @return Random letter
	 */
	public Letter getRandomLetter() {
		if (bag.size() <= 0) {
			//throw new ScrabbleBagException("Bag is empty");
			System.err.println("Bag is empty");
			return null;
		}
		int rand = (int) (Math.random() * bag.size());
		Letter temp = bag.get(rand);
		bag.remove(rand);
		return temp;
	}
	
	/**
	 * Returns the letter to the bag
	 * @param l
	 */
	public void returnLetter(Letter l) {
		if (bag.size() > Constants.BAG_SIZE) {
			System.err.println("Bag has reached maximum size");
			return;
		}
		if (countLetter(l) >= l.getTotalPieces()) {
			System.err.println("Nocando. Bag is already full of " + l.getLetter());
			return;
		}
		bag.add(l);
	}
	
	private int countLetter(Letter l) {
		int count = 0;
		for (int i = 0; i < bag.size(); i++) {
			if (bag.get(i) == l) count++;
		}
		return count;
	}
	
	public String toString() {
		String out = "Size : " + bag.size() + "\n";
		
		for (int i = 0; i < bag.size(); i++) {
			out += bag.get(i) + "\n";
		}
		return out;
	}
}