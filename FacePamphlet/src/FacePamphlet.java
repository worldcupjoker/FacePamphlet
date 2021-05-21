/* 
 * File: FacePamphlet.java
 * -----------------------
 * When it is finished, this program will implement a basic social network
 * management system.
 */

import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;

public class FacePamphlet extends Program 
					implements FacePamphletConstants {

	/**
	 * This method has the responsibility for initializing the 
	 * interactors in the application, and taking care of any other 
	 * initialization that needs to be performed.
	 */
	public void init() {
		// You fill this in
		this.setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
		addFields();
		addActionListeners();
		add(canvas);
    }
    
  
    /**
     * This class is responsible for detecting when the buttons are
     * clicked or interactors are used, so you will have to add code
     * to respond to these actions.
     */
    public void actionPerformed(ActionEvent e) {
		// You fill this in as well as add any additional methods
    	String cmd = e.getActionCommand();
		if (e.getSource() == statusText || cmd.equals("Change Status")) {
			changeStatus();
		}
		if (e.getSource() == pictureText || cmd.equals("Change Picture")) {
			changePicture();
		}
		if (e.getSource() == friendText || cmd.equals("Add Friend")) {
			addFriend();
		}
		if (cmd.equals("Add")) {
			addProfile();
		}
		if (cmd.equals("Delete")) {
			deleteProfile();
		}
		if (cmd.equals("Lookup")) {
			lookupProfile();
		}
	}
    
    /* Method: addProfile() */
    private void addProfile() {
    	String text = nameText.getText();
    	if (text.length() != 0) {
    		FacePamphletProfile profile = new FacePamphletProfile(text);
    		if (myDatabase.containsProfile(text) == false) {
    			myDatabase.addProfile(profile);
    			currentProfile = profile;
    			printCurrentProfile();
    		} else {
    			currentProfile = myDatabase.getProfile(text);
    			printCurrentProfile();
    			msg = "Profile for " + text + " already exists.";
    			canvas.showMessage(msg);
    		}
    	}
    }
    
    /* Method: deleteProfile() */
    private void deleteProfile() {
    	String text = nameText.getText();
    	if (text.length() != 0) {
    		if (myDatabase.containsProfile(text) == true) {
    			myDatabase.deleteProfile(text);
    			updateFriendsList(text);
    			currentProfile = null;
    			printCurrentProfile();
    		} else {
    			currentProfile = null;
    			printCurrentProfile();
    			msg = "Profile with name " + text + " does not exist";
    			canvas.showMessage(msg);
    		}
    	}
    }
    
    /* Method: updateFriendsList(String name) */
    /**
     * This method removes name from all associated friends lists.
     * @param name
     */
    private void updateFriendsList(String name) {
    	Iterator<String> it = myDatabase.getDatabase().keySet().iterator();
    	while (true) {
			if (it.hasNext() == false) {
				break;
			}
			FacePamphletProfile profile = myDatabase.getProfile(it.next());
			Iterator<String> it2 = profile.getFriends();
			while (true) {
				if (it2.hasNext() == false) {
					break;
				}
				String friend = it2.next();
				if (name.equals(friend)) {
					//profile.removeFriend(name); // Can't remove this mapping now. It messes up the iterator.
					break;
				}
			}
			profile.removeFriend(name);
		}
    }
    
    /* Method: lookupProfile() */
    private void lookupProfile() {
    	String text = nameText.getText();
    	if (text.length() != 0) {
    		if (myDatabase.containsProfile(text) == true) {
    			FacePamphletProfile profile = myDatabase.getProfile(text);
    			currentProfile = profile;
    			printCurrentProfile();
    		} else {
    			currentProfile = null;
    			printCurrentProfile();
    			msg = "Lookup: profile with name " + text + " does not exist";
    			canvas.showMessage(msg);
    		}
    	}
    }
    
    /* Method: changeStatus() */
    private void changeStatus() {
    	String text = statusText.getText();
    	if (text.length() != 0) {
    		if (currentProfile == null) {
    			msg = "No current profile. Please select a profile.";
    			canvas.showMessage(msg);
    		} else {
    			currentProfile.setStatus(text);
    			printCurrentProfile();
    			msg = "Status updated to " + text;
    			canvas.showMessage(msg);
    		}
    	}
    }
    
    /* Method: changePicture() */
    private void changePicture() {
    	String text = pictureText.getText();
    	if (text.length() != 0) {
    		if (currentProfile == null) {
    			msg = "No current profile. " + "Please select a profile.";
    			canvas.showMessage(msg);
    		} else {
    			GImage image;
    			try {
    				image = new GImage(text);
        			currentProfile.setImage(image);
        			printCurrentProfile();
        			msg = "Picture updated";
    				canvas.showMessage(msg);
    			} catch (ErrorException ex) {
    				msg = "Type in a valide file name.";
    				canvas.showMessage(msg);
    			}
    		}
    	}
    }
    
    /* Method: addFriend() */
    private void addFriend() {
    	String text = friendText.getText();
    	if (text.length() != 0) {
    		if (currentProfile == null) {
    			msg = "No current profile. " + "Please select a profile.";
    			canvas.showMessage(msg);
    		} else if (text.equals(currentProfile.getName())) {
    			msg = "You can't add yourself as friend.";
    			canvas.showMessage(msg);
    		} else {
    			if (myDatabase.containsProfile(text) == false) {// If the name is in the database.
    				msg = "No such profile found.";
    				canvas.showMessage(msg);
    			} else {// Check if they are already friends.
    				Iterator<String> it = currentProfile.getFriends();
    				if (checkFriends(text, it)) {
    					msg = currentProfile.getName() + " is already a friend with " + text + ".";
    					canvas.showMessage(msg);
    				} else {
    					currentProfile.addFriend(text);
    					myDatabase.getProfile(text).addFriend(currentProfile.getName());
    					printCurrentProfile();
    					msg = text + " added as friend";
        				canvas.showMessage(msg);
    				}
    			}
    		}
    	}
    }
    
    /* Method: printCurrentProfile() */
    private void printCurrentProfile() {
    	canvas.displayProfile(currentProfile);
    }
    
    /* Method: checkFriends() */
    private boolean checkFriends(String text, Iterator<String> it) {
    	while (true) {
			if (it.hasNext() == false) {
				return false;
			}
			String name = it.next();
			if (text.equals(name)) {
				return true;
			}
		}
    }
    
    /* Method: addNorthField() */
    /**
     * Add essential text fields and buttons in the north area and the west area.
     */
    private void addFields() {
    	
    	// North area.
    	add(new JLabel("Name"), NORTH);
    	nameText = new JTextField(TEXT_FIELD_SIZE);
		add(nameText, NORTH);
		nameText.addActionListener(this);
		add(new JButton("Add"), NORTH);
		add(new JButton("Delete"), NORTH);
		add(new JButton("Lookup"), NORTH);
		
		// South area
		statusText = new JTextField(TEXT_FIELD_SIZE);
		add(statusText, WEST);
		statusText.addActionListener(this);
		add(new JButton("Change Status"), WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
		pictureText = new JTextField(TEXT_FIELD_SIZE);
		add(pictureText, WEST);
		pictureText.addActionListener(this);
		add(new JButton("Change Picture"), WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
		friendText = new JTextField(TEXT_FIELD_SIZE);
		add(friendText, WEST);
		friendText.addActionListener(this);
		add(new JButton("Add Friend"), WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
    }
    
    /* Instance variables */
    private JTextField nameText, statusText, pictureText, friendText;
    private FacePamphletDatabase myDatabase = new FacePamphletDatabase();
    private FacePamphletProfile currentProfile;
    private FacePamphletCanvas canvas = new FacePamphletCanvas();
    private String msg;
}
