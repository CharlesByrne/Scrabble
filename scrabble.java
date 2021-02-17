/////////////// #######################################################
//		SCRABBLE - FINAL
//		BY CHARLES BYRNE
//		HIGHER DIPLOMA IN COMPUTER SCIENCE - 18th January 2021
//
//		CS210 - Algorithms and Data Structures - end of semester assignment
//
//		By Charles Byrne 
//
//		97700266

//		TESTED IN ECCLIPSE
//
//		DICTIONARY: "words.txt" - CHANGE LINE 50
/////////////// #######################################################

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.Scanner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

//	########################################################
//		DICTIONARY CLASS
//	########################################################

class Dictionary{
	
	public static String dictionaryURL = "words.txt";
	
	static int[] dictionary_index;
 public static String words[]; 
 Dictionary_LinkedList words_LL = new Dictionary_LinkedList();
  

 public Dictionary(){

 	String filename = new String(dictionaryURL);
 	
		Scanner input = new Scanner(System.in);
		
 	do {
     	System.out.println("Loading dictionary: " + filename);
 		words = load_order_and_save(filename);
	    	if (words == null) {
	    		System.out.print("Please enter the dictionary directory and filename: ");
	    		filename = input.nextLine();
	    	}
 	} while (words==null);
 	input.close();
		dictionary_index = createIndex();
 	
 } // Dictionary Constructor
 
 public static int getSize(){
     return words.length;
 }
 
 public String getWord_LL(int n){
     return words_LL.get(n);
 }
 
 public static String getWord(int n){
	 if (n>=0 && n<words.length)
		 return words[n];
	 else
		 return null;
 }
 
	public byte strCompare(String str1, String str2) {
	    int len1 = str1.length();
	    int len2 = str2.length();
	    int s_len, i;
	    byte s_lw;
	    byte precedence = 0;
	    boolean sorted = false;
	    char c1, c2;

	    // find the shortest word
	    
	    if (len1 > len2) {
	        s_len = len2;
	        s_lw = 1;
	    } else if (len1 < len2) {
	        s_len = len1;
	        s_lw = 2;        
	    } else {
	        s_len = len1;
	        s_lw = 0;
	    }
	    i = 0;
	    while ((!sorted) && (i<s_len)) {
	        c1 = Character.toUpperCase(str1.charAt(i));
	        c2 = Character.toUpperCase(str2.charAt(i));
	        
	        if (c1<c2) {
	            sorted = true;
	            precedence = 2;
	            //System.out.println(c1 + " < " + c2);
	        } else if (c1>c2) {
	            sorted = true;
	            precedence = 1;
	            //System.out.println(c1 + " > " + c2);
	        } /*else {
	            System.out.println(c1 + " = " + c2);
	        }*/
	        i++;
	    }
	    if (!sorted) {
	        //System.out.println(" BIGGER " + s_lw);
	        precedence = s_lw;
	    }

	    return precedence;
	}
	
	public boolean validString(String str1) { // valid word for Scrabble
	    int len1 = str1.length();
	    if (len1<2)
	    	return false;
	    char c;
	    for (int i=0; i<len1; i++) {
	    	c = str1.charAt(i);
	    	if (c<65 || (c>90 && c<97) || c>122)
	    		return false;
	    }
	    return true;
	}
	
	
 private String[] load_order_and_save(String file) {
 	
     File aFile = new File(file);     
     StringBuffer contents = new StringBuffer();
     BufferedReader input = null;
     
     try {
         input = new BufferedReader( new FileReader(aFile) );
         String line = null; 

         while (( line = input.readLine()) != null){
             contents.append(line);

             contents.append(System.getProperty("line.separator"));
         }
     } catch (FileNotFoundException ex){
         System.out.println("Can't find the file '" + file + "' - are you sure the file is in this location: "+file);
         
         return null;
     } catch (IOException ex){
         System.out.println("Input output exception while processing file");
         ex.printStackTrace();
     } finally{
         try {
             if (input!= null) {
                 input.close();
             }
         }catch (IOException ex){
             System.out.println("Input output exception while processing file");
             ex.printStackTrace();
         }
     }
     String[] array = contents.toString().split("\r\n");
     String curWord = new String("");
     Dictionary_Link word1;
     String word1_txt = new String("");
     
     int len = array.length;
     int newLen = 0;
		System.out.println("Dictionary successfully loaded: " + len + " words.");
     for(int i = 0; i<len; i++){
     	curWord = array[i].trim().toLowerCase();
     	if (validString(curWord)) {
	        	word1=this.words_LL.getLast();
	        	if (word1 != null)
	        		word1_txt = word1.getData();
	        	else 
	        		word1_txt = "";
	        	//if (i>470000 && i<471000) 
	        	//	System.out.println("DATA:" + i + " - " + curWord);
	        	if (this.strCompare(word1_txt, curWord) == 2) {  
	        		this.words_LL.add(curWord);
	        		newLen++;
	        	} else {
	        		do {
	        		word1 = word1.getPrevious();
	        		if (word1 != null)
	        		word1_txt = word1.getData();
	        			else 
	        				word1_txt = "";
	        		} while (this.strCompare(word1_txt, curWord) == 1);
	        		
	        		this.words_LL.insert(curWord,word1);
	        		newLen++;
	        	}
	        	// need a function called addinPlace to order new word
	        	// in alphabetical order

     	} else {
     		//System.out.println("EXCLUDED:" + curWord);
     	}
     }
     String[] dictionaryOrdered = new String[newLen];
 	word1=this.words_LL.getFirst();

 	// discard the linked list:
 	Dictionary_Link last_word;
 	int i = 0;
 	
 	while (word1!=null) {
     	dictionaryOrdered[i] = new String(word1.getData());
     	last_word = word1;
     	word1=word1.getNext();
     	last_word.discard();
     	last_word = null;
     	i++;
     }
 	words_LL = null;
     return dictionaryOrdered;

 }
 
	public boolean findWordFast(String word) {

		String curWord = new String("");
		
		if (word == null)
				return false;
		int len = word.length();
		if (len<2)
			return false;
		
		int i1, i2,r1,r2;
		
		i1 = (int)word.charAt(0)-97; // a - z - will be lower case = 97 - 122
		i2 = (int)word.charAt(1)-97; // a - z - will be lower case = 97 - 122
		if (i1<0 || i1>25 || i2<0 || i2>25)
			return false;
	
		
		r1 = dictionary_index[(i1*26)+i2];

		// get next bound - e.g. AA - AB or AZ - BA or ZZ to end
		// the index is 26 * 26 + 1 for the total size at the end
		do {
			if (i2<25)
				i2++;
			else {
				i2 = 0;
				i1++;
			}
			r2 = dictionary_index[(i1*26)+i2];
		} while (r2==-1);
		for (int i=r1; i<r2; i++) {
			curWord = getWord(i);
			if (curWord == null)
				return false;
			if (curWord.equals(word)) {

				return true;
			}
		}

		return false;
	}
 
	
	public static String removeCR(String str1) {
		String retStr = new String("");
		int i;
		char c;
		int len = str1.length();
		for (i=0; i<len; i++) {
			c = str1.charAt(i);
			if (c>31) {
				retStr+=c;
			}
		}
		return retStr;
	}
	
	public static int find_first(char c1, char c2, int startAt) {
		int i,dS;
		dS = getSize();

		char cw1, cw2;
		String dW = new String();

		i=startAt;
		while (i<dS) {

			dW = getWord(i);
			if (dW.length()>0) 
				cw1=dW.charAt(0);
			else
				cw1=' ';
			if (dW.length()>1) 
				cw2=dW.charAt(1);
			else
				cw2=' ';			
			if ((cw1==c1) && (cw2==c2)) {
				return i;
			} else {
				i++;
			}
		}
		return -1;
	}
	
	public static int[] createIndex() {
		
		int[] returnIndex = new int[677];
		int c = 0,c2 = 0;
		int index;

		index = 0;
		for (int ii=0; ii<26; ii++) {

			for (int i=0; i<26; i++) {
			c2 = find_first((char)(97+ii),(char)(97+i),c);
			if (i>0)

			if (c2>=0) {
				returnIndex[index] = c2;
				c = c2;
			} else
				returnIndex[index] = -1;
			index++;
		} // inner for loop

		} // outer for loop

		returnIndex[676]= words.length;

		return returnIndex;
	}
	
} // END: DICTIONARY CLASSS

//########################################################

// DICTIONARY LINKED LIST - ONLY TEMPORARILY TO CLEAN & ORDER DICTIONARY  

class Dictionary_Link {
	 public String data;
	 public Dictionary_Link previous;
	 public Dictionary_Link next;

	 Dictionary_Link(String data) {
	     this.data = data;
	     this.previous = null;
	     this.next = null;
	 }
	 
	 public void discard() {
	     this.data = null;
	     this.previous = null;
	     this.next = null;
	 }
	 
	 public Dictionary_Link getPrevious() {
	     return this.previous;
	 }
	 
	 public Dictionary_Link getNext() {
	     return this.next;
	 }
	 public Dictionary_Link setNext(Dictionary_Link next) {
	     return this.next=next;
	 }
	 
	 public Dictionary_Link setPrevious(Dictionary_Link previous) {
	     return this.previous = previous;
	 }
	 
	 public String getData() {
	     return this.data;
	 }        
	}

	class Dictionary_LinkedList {
	 private Dictionary_Link first;
	 private Dictionary_Link last;
	 private int size;

	 Dictionary_LinkedList() {
	     this.first = null;
	     this.last = null;
	     this.setSize(0);
	 }

	 public boolean isEmpty() {
	     return (this.first==null);
	 }
	 
	 public Dictionary_Link getFirst() {
	     return this.first;
	 }
	 
	 public Dictionary_Link getLast() {
	     return this.last;
	 }
	 
	 public void setLast(Dictionary_Link last) {
	     this.last = last;
	 }
	 
	 public void setFirst(Dictionary_Link first) {
	     this.first = first;
	 }        
	 
	 public String get(int n) {
	     int i;
	     Dictionary_Link pointer = this.first;
	     i=0;
	     while ((pointer != null) && (i<n)) {
	         pointer = pointer.getNext();
	         i++;
	     } 
	     return pointer.getData();
	 }
	 public void add(String str1) {
	     Dictionary_Link newData = new Dictionary_Link(str1);
	     if (getSize()==0) {
	         this.setFirst(newData);
	     } else {
	         this.last.setNext(newData);
	     }
	     this.setLast(newData);
	     this.setSize(this.getSize() + 1);
	 }
	 
	 public void insert(String str1, Dictionary_Link afterThis) {
	     Dictionary_Link newData = new Dictionary_Link(str1);

	     if (afterThis == null || this.last==afterThis) {
	     	add(str1);
	     } else {
	         Dictionary_Link nextData = afterThis.getNext();
	     	
	     	nextData.setPrevious(newData);
	     	newData.setPrevious(afterThis);
	     	newData.setNext(afterThis.getNext());
	     	afterThis.setNext(newData);
		        this.setSize(this.getSize() + 1);
	     }
	 }
	 
	 public String remove() {
	     String retStr = new String("");
	     
	     if (this.first==null) {
	         retStr = null;
	     } else {
	         retStr = this.first.getData();
	         this.first = this.first.next; //temp;
	         // dispose of first
	         if (getSize()==1) {
	             this.last = null; // there won't be anything left
	         }            
	     }
	     this.setSize(this.getSize() - 1);
	     return retStr;
	 }

		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}    
	 
	 
	}


	//########################################################

class ScrabbleWord {
	
	
	public String word;
	public int score;
	public int x1, y1, x2, y2;
	     	
	
	
	

	ScrabbleWord() {
		super();
		this.word = "";
		this.score = 0;
		this.x1 = 0;
		this.y1 = 0;
		this.x2 = 0;
		this.y2 = 0;
	}
	
	
	
	public void setVars(String word, int score, int x1, int y1, int x2, int y2) {
		this.word = word;
		this.score = score;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
}

class Link {
    public String data;
    public Link next;

    Link(String data) {
        this.data = data;
        this.next = null;
    }
    
    public Link getNext() {
        return this.next;
    }
    public Link setNext(Link next) {
        return this.next=next;
    }        
    
    public String getData() {
        return this.data;
    }        
}

class LinkedList {
    private Link first;
    private Link last;
    private int size;

    LinkedList() {
        this.first = null;
        this.last = null;
        this.size = 0;
    }

    public boolean isEmpty() {
        return (this.first==null);
    }
    
    public Link getFirst() {
        return this.first;
    }
    
    public void setLast(Link last) {
        this.last = last;
    }
    
    public void setFirst(Link first) {
        this.first = first;
    }        
    
    public String get(int n) {
        int i;
        Link pointer = this.first;
        i=0;
        while ((pointer != null) && (i<n)) {
            pointer = pointer.getNext();
            i++;
        } 
        return pointer.getData();
    }
    public void add(String str1) {
        Link newData = new Link(str1);
        if (size==0) {
            this.setFirst(newData);
        } else {
            this.last.setNext(newData);
        }
        this.setLast(newData);
        this.size++;
    }
    
    public String remove() {
        String retStr = new String("");
        //Link temp = new Link("");
        
        if (this.first==null) {
            retStr = null;
        } else {
            retStr = this.first.getData();
            this.first = this.first.next; //temp;
            // dispose of first
            if (size==1) {
                this.last = null; // there won't be anything left
            }            
        }
        this.size--;
        return retStr;
    }    
    
    
}

public class Scrabble extends JFrame {
	
	private static boolean isQuiet = true;	// set to false to DEBUG
	
    public static void printIf(String s) {
        if (!isQuiet) {
            System.out.println(s);
        }
    }	
	
	public static Dictionary dictionary;


	
	public static void setLowerDisplay(String str2) {
		String str1 = new String("");
		str1 = bottomDisplay.getText();
		if (str1.length()>200) {
			str1=str1.substring(0,120);
		}
		str1 = str2 + " " + str1;
		bottomDisplay.setText(str1);
	}
	
	
	public static String removeCR(String str1) {
		String retStr = new String("");
		int i;
		char c;
		int len = str1.length();
		for (i=0; i<len; i++) {
			c = str1.charAt(i);
			if (c>31) {
				retStr+=c;
			}
		}
		return retStr;
	}
	
	static int temp_inc = 0; // this is for the testing process - DELETE AFTER
	static int[] totalScore = {0,0};
	static boolean twoPlayerGame = false;
	static String wordsStatus = new String("");
	static int save_cpScore = 0;
	static int cp_move_index = 0;
	static int welcome_index = 0;
	static int[] welcome_place = {80,81,82,83,84,85,86,112,113,139,140,141,142,143,144,145,146};
	static char[] welcome_text = {'W','E','L','C','O','M','E','T','O','S','C','R','A','B','B','L','E'};
	static char[] welcome_jumble = new char[17];
	static boolean disableBoard = false;
	static int currentPlayer = 0;
	static ScrabbleWord[] wordsList = new ScrabbleWord[10];
	static int scrabblewordCount = 0;
	static JButton[] scrabbleBoard_buttons = new JButton[225];
	static JButton[] playerRackButtons = new JButton[7];
	static JButton showHideButton;
	static String[] showHide = {"HIDE","SHOW"};
	static int skipCount[] = {0,0};
	static int[] pickedLetters = new int[2];
	static boolean replaceTilesButtonPressed = false;
	static boolean[] tilesToReplace = {false,false,false,false,false,false,false};
	static boolean jumbling = false;
    
	private static JLabel bottomDisplay, topDisplay, playerTile;
	
    private static JLabel[] scoreBoard = new JLabel[2];
    
	public static char[] scrabbleBoard = new char[225]; // 15 * 15
	
	public static int[] letterQuantities = {9,2,2,4,12,2,3,2,9,1,1,4,2,6,8,2,1,6,4,6,4,3,3,1,2,1,2};
	
	// this was a WILDCARDS test - 	public static int[] letterQuantities = {2,2,2,4,12,2,3,2,9,1,1,4,2,6,8,2,1,6,4,6,4,3,3,1,2,1,9};
	
	public static int[] letterValues = {1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10};
	public static int[] scrabbleBoard_special = {
	4,0,0,1,0,0,0,4,0,0,0,1,0,0,4,
	0,3,0,0,0,2,0,0,0,2,0,0,0,3,0,
	0,0,3,0,0,0,1,0,1,0,0,0,3,0,0,
	1,0,0,3,0,0,0,1,0,0,0,3,0,0,1,
	0,0,0,0,3,0,0,0,0,0,3,0,0,0,0,
	0,2,0,0,0,2,0,0,0,2,0,0,0,2,0,
	0,0,1,0,0,0,1,0,1,0,0,0,1,0,0,
	4,0,0,1,0,0,0,3,0,0,0,1,0,0,4,
	0,0,1,0,0,0,1,0,1,0,0,0,1,0,0,				
	0,2,0,0,0,2,0,0,0,2,0,0,0,2,0,
	0,0,0,0,3,0,0,0,0,0,3,0,0,0,0,
	1,0,0,3,0,0,0,1,0,0,0,3,0,0,1,
	0,0,3,0,0,0,1,0,1,0,0,0,3,0,0,
	0,3,0,0,0,2,0,0,0,2,0,0,0,3,0,	
	4,0,0,1,0,0,0,4,0,0,0,1,0,0,4};
	public static int[] pouch = new int[102];
	public static int pouchLen = 0;
	public static int lastSquarePressed = 0;
	
    private static final String ID_PROPERTY_2 = "ID_PROPERTY_2";
	private static final String ID_PROPERTY = "ID_PROPERTY";
    private static final String WILDCARD_PROPERTY = "WILDCARD_PROPERTY";
    
	public static char wcVal = ' ';
    public static Scrabble scrabbleBoardWindow;
	static JFrame wildcardWindow = new JFrame("Pick a Wildcard Letter");
	static JFrame welcomeScreen = new JFrame("Welcome to Scrabble");
	static JFrame pickRandomLetter = new JFrame("Pick A Random Letter");
	static JLabel[] db_textArea = new JLabel[6];
	static JButton[] db_button = new JButton[5];
	static JButton buttonReplaceTiles;
    static JButton buttonPlay, buttonUndo, buttonExit, buttonSkip;
	static int pickingLetters = 0;
	

	static JButton[] wcButton = new JButton[26];
	
    private static char[][] pieces = new char[2][7];
    private static char[] computerTileChosen = new char[7]; // which tile chosen
    private static int computerRackSize = 7;
    private static char pickedPiece = '-';
    private static int pickedPieceNum = -1;
    private static int[] moves = new int[14];
    private static int movesCount = 0;
    private static boolean firstPlay = true;
    
    public static void getPieces() {
    	int t, i;
    	
    	jumblePouch();
    	for (i=0; i<7; i++) {
    		t = takeFromPouch();
    		pieces[0][i]=getLetter(t);
    		t = takeFromPouch();
    		pieces[1][i]=getLetter(t);
    		
    		printIf(i + ". NEW PIECES: "+pieces[0][i] + ";" + pieces[1][i]);
    		//System.out.println(pouchLen);
    	}
    }

    public static void main(String[] args) {
     
    	 dictionary = new Dictionary();
    	
    	fillPouch();
    	jumblePouch();
    	getPieces();
    	
    	int i;
    	
    	// this array holds found words in a move
    	
    	for (i=0; i<10; i++) {
    		wordsList[i] = new ScrabbleWord();
    	}
    	
    	
        SwingUtilities.invokeLater(new Runnable() {
        	
            public void run() {
            	scrabbleBoardWindow = new Scrabble();
                scrabbleBoardWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               
                scrabbleBoardWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
                
        		showHideRack();
        		buttonReplaceTiles.setVisible(false);
        		buttonPlay.setVisible(false);
        		buttonUndo.setVisible(false);
        		buttonExit.setVisible(false);
        		buttonSkip.setVisible(false);
            	showHideButton.setVisible(false);
        		
                scrabbleBoardWindow.setVisible(true);
                scrabbleBoardWindow.setAlwaysOnTop(true); 
                welcomeIntro.start();

            }
        });
        
    	welcomeScreen.setExtendedState(JFrame.MAXIMIZED_BOTH);
    	welcomeScreen.setLayout(new GridLayout(3, 1));
 
    	JButton welcome1Player = new JButton("1 Player Game");
    	welcome1Player.putClientProperty(ID_PROPERTY,1);
    	welcome1Player.addActionListener(pick1or2Players_Listener);
        welcomeScreen.add(welcome1Player);
        
        pickRandomLetter.setExtendedState(JFrame.MAXIMIZED_BOTH);
        pickRandomLetter.setLayout(new GridLayout(5, 1));

        db_textArea[4] = new JLabel(" ");
        db_textArea[4].setFont(new Font("Dialog", Font.BOLD, 18));
        db_textArea[4].setText(" ");
        db_textArea[4].setHorizontalAlignment(SwingConstants.CENTER); 
        pickRandomLetter.getContentPane().add(db_textArea[4]);
        
        db_textArea[3] = new JLabel(" ");
        db_textArea[3].setFont(new Font("Dialog", Font.BOLD, 18));
        db_textArea[3].setText(" ");
        db_textArea[3].setHorizontalAlignment(SwingConstants.CENTER);
        
        db_textArea[0] = new JLabel(" ");
        db_textArea[0].setFont(new Font("Dialog", Font.BOLD, 18));
        db_textArea[0].setText(" Player 1 pick a letter... ");
        db_textArea[0].setHorizontalAlignment(SwingConstants.CENTER); 
        
        db_textArea[5] = new JLabel(" ");
        db_textArea[5].setFont(new Font("Dialog", Font.BOLD, 18));
        db_textArea[5].setText(" ");
        db_textArea[5].setHorizontalAlignment(SwingConstants.CENTER); 
        
    	db_button[0] = new JButton("CLICK TO PICK!");
    	db_button[0].putClientProperty(ID_PROPERTY,2);
    	db_button[0].addActionListener(goScrabble_Listener);
    	pickRandomLetter.getContentPane().add(db_textArea[5]);
    	pickRandomLetter.getContentPane().add(db_textArea[0]);

    	pickRandomLetter.getContentPane().add(db_textArea[3]);
    	pickRandomLetter.add(db_button[0]);

        
    	db_textArea[2] = new JLabel(" ");
    	db_textArea[2].setFont(new Font("Dialog", Font.BOLD, 18));
    	db_textArea[2].setText("Welcome to Scrabble");
    	db_textArea[2].setHorizontalAlignment(SwingConstants.CENTER);        
       	welcomeScreen.getContentPane().add(db_textArea[2]);

       	
       	
       	JButton welcome2Player = new JButton("2 Player Game");
    	welcome2Player.putClientProperty(ID_PROPERTY,2);
    	welcome2Player.addActionListener(pick1or2Players_Listener);
        welcomeScreen.add(welcome2Player);

    	wildcardWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
    	wildcardWindow.setLayout(new GridLayout(3, 1));

    	db_textArea[1] = new JLabel(" ");
    	db_textArea[1].setFont(new Font("Dialog", Font.BOLD, 18));
    	db_textArea[1].setText("PICK:");
    	wildcardWindow.getContentPane().add(db_textArea[1]);
    	
    	char c;
    	for (i=0; i<26; i++) {
    		c = (char)(65+i);
        	wcButton[i] = new JButton(""+c);
        	wildcardWindow.getContentPane().add(wcButton[i]);
        	wcButton[i].putClientProperty(WILDCARD_PROPERTY, c);
        	wcButton[i].addActionListener(wcButtonListener);
    	}
    


    	
    } // main method
    
    public static char getLetter(int n) {
    	if (n == -1)
    		return '-';
    	else if (n == 26)
    		return ' ';
    	else return (char)(n+65);
    }
    
    public static int getCode(char c) {
    	// used to swap pieces in pouch
    	// and calculate the score at the end of the game
    	
    	// chars 0-25 = a-z 26 = blank tile
    	// what about - (45) ?
    	int retVal = -1;
    	
    	if (c == ' ')
    		retVal = 26;
    	else 
    		retVal = (int)(c-65);
    	
    	if (retVal<0 || retVal>25)
    		retVal = -1; // could put -1 here but check doesn't cause overflow error
    	
    	return retVal;
    }
    
    public static int getValue(char c) {
    	// used to find score of any char
    	// 97 - lowercase = -1    	

    	if (c>='A' && c<='Z')
    		return letterValues[(c-65)];
    	else 
    		return -1;

    } // getValue
    
    public static void fillPouch() {
    	
    	// chars 0-25 = a-z 26 = blank tile
    	
    	int len = letterQuantities.length;
    	int i = 0;
    	int ii = 0;
    	int i3;
    	pouchLen = 0;
    	while (i<102) {
    		for (i3=0; i3<letterQuantities[ii]; i3++) {
    			pouch[i]=ii;
    			//System.out.print(i+" "+getLetter(pouch[i])+"; ");
    			i++;
        		pouchLen++;
    		}
    		ii++;
    	} // while loop
    	jumblePouch();
    	// TO-DO: END OF GAME TEST: 
    	//  pouchLen = 20;
    	
    } // fillPouch
    
    public static void jumblePouch() {
    	int i = 0;
    	int rand1,rand2;
    	int temp;
    	Random r = new Random();
    	for (i=0; i<pouchLen; i++) {
			rand1 = r.nextInt(pouchLen);
			rand2 = r.nextInt(pouchLen);
			if (rand1 != rand2) {
    			temp = pouch[rand1];
    			pouch[rand1] = pouch[rand2];
    			pouch[rand2] = temp;
			}
    	}
    	//System.out.println("\nPL:"+pouchLen);
	//for (i=0; i<pouchLen; i++) {
	//	System.out.print(i+" "+getLetter(pouch[i])+"; ");
//	}
}
    
    public static void addToPouch(int tile) {
    	
    	if (pouchLen < pouch.length) { // ensure there is no overflow
	    	pouchLen++;
	    	pouch[pouchLen-1] = tile; // put the piece to the back of the pouch
	    	
	    	jumblePouch(); // shake the pouch
    	}
    } // addToPouch
    
    public static int takeFromPouch() {
    	int i = 0;
    	int rand1;
    	int lastTile,temp;
    	
    	// if empty return -1
    	if (pouchLen==0)
    		return -1;
    	else if (pouchLen==1) {
    		lastTile = 0;     		
    	} else {
    	
	    	// shake the pouch
	    	jumblePouch();
	    	
	    	// choose a random tile
	    	
	    	Random r = new Random();
			rand1 = r.nextInt(pouchLen);
			lastTile = pouchLen-1;
			
			// put it to the back of the pouch
			
			if (rand1 != lastTile) {
				temp = pouch[rand1];
				pouch[rand1] = pouch[lastTile];
				pouch[lastTile] = temp;
			}
    	} // if pouch has more that 1 item
    	
    	pouchLen--;
    	
    	printIf("\nPL:"+pouchLen);
		//System.out.print(lastTile+" "+getLetter(pouch[lastTile])+"; ");
		return pouch[lastTile];
    } // takeFromPouch
    
    // COMPUTER RACK: Put empty spaces in rack at end, so CP won't take them
    
    public static void computersRack_PutBlanksAtBack() {
        
    int i = 0;
    int spaceAvailable = -1;

        while (i < 7) {
            if (pieces[1][i]=='-') {
                if (spaceAvailable==-1)
                    spaceAvailable = i;
            } else {
                if (spaceAvailable !=- 1) {
                    // switch
                	pieces[1][spaceAvailable] = pieces[1][i];
                	pieces[1][i] = '-';
                    i = spaceAvailable;
                    spaceAvailable = -1;
                }
            }
            
            i++;
        }
        if (spaceAvailable>-1) // SET COMPUTER RACK_SIZE !!!
        computerRackSize = spaceAvailable;
    }
    
    // fill the player's rack after they have placed pieces on the board
    
    public static void fillPieces(int playerNumber, boolean isComputer) {
    	int i,t,endVal;
    	boolean pouchEmpty = false;
    	int toBeReplaced = 0;
    	for (i=0; i<7; i++) {
    		if (pieces[playerNumber][i]=='-')
    			toBeReplaced++;
    	}
    	if (toBeReplaced==7 && pouchLen==0)
    		endGame(0);
    	else {
    		i = 0;
    		while (i<7 && !pouchEmpty) {
        		printIf("P"+i+" :"+pieces[playerNumber][i]);
        		if (pieces[playerNumber][i]=='-') {
    	    		t = takeFromPouch(); // take a letter from the pouch
    	    		if (t==-1) {
    	    			
    	    			// the pouch is empty 
    	    			pouchEmpty = true; // pouch is empty;
    	    		} else {
    	    			pieces[playerNumber][i]=getLetter(t);
    	    			
    	    			if (!isComputer) {
	    	    			playerRackButtons[i].putClientProperty(ID_PROPERTY_2,pieces[playerNumber][i]);
	    	    			playerRackButtons[i].setText(""+showButton(pieces[playerNumber][i], ((int)pieces[playerNumber][i])-65));
    	    			}
    	    			
    	    		} // END: inner else clause
        		} // END: if clause
        		
        		i++;
        	} // END: while loop    		

			// IMPORTANT: Order pieces so that empty ones are at the end
			// set computer rack size (i.e., not the usual 7)
    		// This is important for near end-of-game play
    		
    		if (isComputer && pouchEmpty)
    			computersRack_PutBlanksAtBack();

    	
    	} // END: if there are tiles

    	
		// if less than 7 tiles in pouch you cannot replace your tiles
    	
    	if (pouchLen<7)
    		buttonReplaceTiles.setVisible(false);
    }

    public Scrabble() {
    	
    	
        super("Scrabble");

        JPanel primaryPanel = new JPanel(new BorderLayout());
        JPanel scrabblePanel = buildscrabblePanel();
        JPanel sidebar = buildsidebar();

        primaryPanel.add(scrabblePanel, BorderLayout.CENTER);
        primaryPanel.add(sidebar, BorderLayout.EAST);
        
        bottomDisplay = new JLabel(" ");
        bottomDisplay.setFont(new Font("Dialog", Font.BOLD, 18));
        primaryPanel.add(bottomDisplay, BorderLayout.SOUTH);

        topDisplay = new JLabel(" ");
        topDisplay.setFont(new Font("Dialog", Font.BOLD, 18));


        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(primaryPanel, BorderLayout.CENTER);
        getContentPane().add(topDisplay, BorderLayout.NORTH);
	        
        for (int i=0; i<225; i++) 
        	scrabbleBoard[i] = ' ';
        pack();
        
    }

    private final ActionListener scrabbleBoard_listener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	if (!disableBoard && pickedPiece!='-') {
	            JComponent source = (JComponent)e.getSource();
	            Integer number = (Integer) source.getClientProperty(ID_PROPERTY);
	            int tX,tY;
	            tY = number / 15;
	            tX = number % 15;  
	            printIf(""+number + ". " + tX + ", " + tY);
	            scrabbleBoardPressed(source, number.intValue());
        	}
        }
    };
    
    private final ActionListener pickFromRack_listener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	
            JComponent source = (JComponent)e.getSource();
            		//(char) source.getClientProperty(ID_PROPERTY_2);
            int n = (int) source.getClientProperty(ID_PROPERTY);
            char c = pieces[currentPlayer][n];

            
            printIf("PICKER:-: "+ c + "; n:" + n);
            printIf(""+pieces[currentPlayer][n]);
            printIf(""+pieces[0][n]);
        	// ABAB
        	if (!disableBoard) {
        		if (replaceTilesButtonPressed) {
        			//buttonReplaceTiles
        			if (!tilesToReplace[n]) {
            			playerRackButtons[n].setOpaque(true);
            			playerRackButtons[n].setBackground(Color.CYAN);
            			tilesToReplace[n] = true;        				
        			} else {
            			playerRackButtons[n].setOpaque(false);
            			playerRackButtons[n].setBackground(null);
            			tilesToReplace[n] = false;          				
        			}

        		} else {
		   //         System.out.println("PICKER PRESSED: " +  .getText());
		            pickedPiece = c;
		            pickedPieceNum = n;
		            printIf("PICKER PRESSED: " + pickedPieceNum + " - " + pickedPiece);
        		}
        	}
        }
    };

    
    private final ActionListener replaceTilesListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {

        	int i;
        	int replacedTilesCount=0;
        	int[] replacedTiles = new int[7]; // = int 0-26 a-z, and space
        	int curTile;
        	
        	if (pouchLen<7) { // remove this, it is redundant as the button disappears
        		topDisplay.setText("There are less than 7 tiles in the pouch, therefore you cannot replace tiles.");
        	} else {
	        	if (!replaceTilesButtonPressed) {        
	        		undoButtonPressed(false);
	        		buttonReplaceTiles.setBackground(Color.cyan);
		        	replaceTilesButtonPressed = true;
	        	} else {
	        		buttonReplaceTiles.setBackground(null);
	        		// swap these tiles with tiles in the pouch and skip move
	        		
	        		for (i=0; i<7; i++) {
	        			if (tilesToReplace[i]) {

	        					printIf(""+i);
	                			playerRackButtons[i].setOpaque(false);
	                			playerRackButtons[i].setBackground(null);
	                			curTile = getCode(pieces[currentPlayer][i]);
	                			if (curTile>=0) {
		                			replacedTiles[replacedTilesCount] = curTile;
		                			pieces[currentPlayer][i] = getLetter(takeFromPouch());
		                			// take from pouch
		                			// then add to pouch at end, jumble
	
		                			tilesToReplace[i]=false;
		                			replacedTilesCount++;
	                			}
	        			}
	        		}
	        		for (i=0; i<replacedTilesCount; i++) {
	        			addToPouch(replacedTiles[i]);
	        		}
	        		// check how many tiles have been selected.
	        		// if zero then ignore
	        		
		        	replaceTilesButtonPressed = false;      
		        	
	        		if (replacedTilesCount>0) {
	        			// skip this player's go
	        			topDisplay.setText("Move forefeited in order to switch tiles.");
	        			skipCount[currentPlayer]=0; // doesn't count as a skip
	        			refreshRack(); 
	                	undoButtonPressed(false);
	                	// MOVE FOREFITED
	                	nextPlayersGo();
	        		}
	        				
	        	}
        }
       } // actionPerformed
    }; // replaceTilesListener

    private final ActionListener undoListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {

    		// you can't press this while CP is making a move
        	// as this would mess things up badly!
        	
        	if (!disableBoard)				
        		undoButtonPressed(false);
        }
    };
    
    private final ActionListener playListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	
    		// you can't press this while CP is making a move
        	// as this would mess things up badly!
        	
        	if (!disableBoard)	
        		playButtonPressed(false,currentPlayer); // false means it is not a computer move
        }
    };
    
    public static void resetScrabbleBoard() {
    	int i;
    	
    	fillPouch();
    	jumblePouch();
    	getPieces();
    	
    	
    	replaceTilesButtonPressed = false;
    	pickedPiece = '-';
    	pickedPieceNum = -1;

    	currentPlayer = 0;
    	movesCount = 0;
    	totalScore[0] = 0;
    	totalScore[1] = 0;
    	skipCount[0] = 0;
    	skipCount[1] = 0;
    	
        for (i=0; i<225; i++) {
        	scrabbleBoard[i] = ' ';
        	setClearTile(i);
        }
        pickingLetters = 0; // pick a letter to go.
        firstPlay = true;
    	scoreBoard[0].setText("0");
    	scoreBoard[1].setText("0");
    	topDisplay.setText("");
    	bottomDisplay.setText("");
    	computerRackSize = 7;
		buttonReplaceTiles.setVisible(true);
    	refreshRack();
    	showHideButton.setVisible(true);

    }
    
    private static void endGame(int reason) {
    	String txt1 = new String("");
    	String txt2 = new String("");
    	String[] scoreSummary = {"",""};
    	int[] piecesLeftScore = {0,0};
    	int[] piecesLeftCount = {0,0};
    	int[] provisionalScore = {totalScore[0],totalScore[1]};
    	int i, ii;
    	int winner;
    	int curPiece, curScore;
    	
    	if (reason == 0)
    		txt1 = "Game ended due to a player using up all their tiles."; 

    	else if (reason == 1) 
    		txt1 = "Game ended due to two successive skipped turns by both players.";
    	
    	// Unplayed Letters: When the game ends, each player's score is 
    	// reduced by the sum of his or her unplayed letters. 
    	

    	
    	// The player with the highest final score wins the game.
    	
    	// In case of a tie, the player with the highest score before 
    	// adding or deducting unplayed letters wins.

    	for (ii=0; ii<2; ii++) {
    		for (i=0; i<7; i++) {
	    		curPiece = getCode(pieces[ii][i]);
	    		if (curPiece>-1) {
	    			piecesLeftCount[ii]++;
	    			curScore = letterValues[curPiece];
	    			piecesLeftScore[ii] += curScore;
	    			scoreSummary[ii] += "" + pieces[ii][i] + "(" + curScore + ") ";    			
	    		}
	
        	} // inner loop (i)
    	}
    	
    	txt1 += "<br> ";
    	txt1 += (twoPlayerGame) ? "Player 1's" : "Your";
    	txt1 += (piecesLeftScore[0]==0) ? " tiles: None left! " : " tiles left: " + scoreSummary[0]+" (" + piecesLeftScore[0] + " points total)";
    	txt1 += "<br> ";
    	txt1 += (twoPlayerGame) ? "Player 2's" : "My";
    	txt1 += (piecesLeftScore[1]==0) ? " tiles: None left! " : " tiles left: " + scoreSummary[1]+" (" + piecesLeftScore[1] + " points total)";
    	
    	// In addition, if a player has used all of his or her letters, 
    	// the sum of the other players' unplayed letters is added to that 
    	// player's score.
    	
    	if (piecesLeftCount[0]==0) {
    		piecesLeftScore[0] += piecesLeftScore[1];
    	} else if (piecesLeftCount[1]==0) {
    		piecesLeftScore[1] += piecesLeftScore[0];
    	}
    	
    	if (piecesLeftCount[0]>0) {
    		piecesLeftScore[0] = 0 - piecesLeftScore[0];
    	}
    	
    	if (piecesLeftCount[1]>0) {
    		piecesLeftScore[1] = 0 - piecesLeftScore[1];
    	}
    	
    	provisionalScore[0] += piecesLeftScore[0];
    	provisionalScore[1] += piecesLeftScore[1];
    	
    	if (provisionalScore[0] == provisionalScore[1]) {
    		if (totalScore[0] == totalScore[1]) {
    			txt2 = "Draw Match!!! " + totalScore[0] + " points each!";
    			winner = -1;
    		} else {
    			winner = (totalScore[0] > totalScore[1]) ? 0 : 1 ;
    			if (twoPlayerGame)
    				txt2 = "Player "+(winner+1)+" wins";
    			else 
    				txt2 = (winner==0) ? "You win" : "I win";
    			
        		txt2 += " with a score of " + totalScore[winner] + " points! ";
        		
    			if (twoPlayerGame)
    				txt2 += "Player " + ((1-winner)+1);
    			else 
    				txt2 += (winner!=0) ? "You" : "I";
            			        			 
            	txt2 += " had " + provisionalScore[((1-winner))] + " points.";
    		}
    	} else {
    		winner = (provisionalScore[0] > provisionalScore[1]) ? 0 : 1 ;
			if (twoPlayerGame)
				txt2 = "Player "+(winner+1)+" wins";
			else 
				txt2 = (winner==0) ? "You win" : "I win";
			
        	txt2 += " with a score of " + provisionalScore[winner] + " points! ";
			if (twoPlayerGame)
				txt2 += "Player " + ((1-winner)+1);
			else 
				txt2 += (winner!=0) ? "You" : "I";
        			        			 
        	txt2 += " had " + provisionalScore[((1-winner))] + " points.";
    	}
    	    	
    	resetScrabbleBoard(); // BE VERY CAREFUL TO INCLUDE ALL VARIABLES
    	
    	txt1 = "<html>" + txt1 + "<br> " + txt2 + "</html>";
    	
    	db_textArea[2].setText(txt1);
    	disableBoard = true;
    	welcomeScreen.setVisible(true);
    	welcomeScreen.setAlwaysOnTop(true);
    	scrabbleBoardWindow.setVisible(false);

    }
    
    private final ActionListener skipGo_listener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	
        	if (!disableBoard) { // stops bad things happening!
            	skipCount[currentPlayer]++;
            	if ((skipCount[0]>1) && (skipCount[1]>1)) {
            		endGame(1);
            	}
            	printIf("CPLAY: "+ currentPlayer+ "; SKIPCOUNT: " + skipCount[currentPlayer]);
            	undoButtonPressed(false);
            	nextPlayersGo();        		
        	}
        }
    };
    
    
    private static void refreshRack() { //changes rack to show player 1 or 2 or refreshes after tiles have been replaced manually
    	for (int i=0; i<7; i++) {
 			playerRackButtons[i].putClientProperty(ID_PROPERTY_2,pieces[currentPlayer][i]);
 			playerRackButtons[i].setText(""+showButton(pieces[currentPlayer][i], ((int)pieces[currentPlayer][i])-65));     		
    	}
    }


    private static void showHideRack() {
    	boolean visible = !playerRackButtons[0].isVisible();
    	int showHideInt = (visible) ? 0 : 1;
    	 showHideButton.setText(showHide[showHideInt]);
    	if (currentPlayer==0) {
        	for (int i=0; i<7; i++) 
        		playerRackButtons[i].setVisible(visible);
    	} else {
    		for (int i=0; i<7; i++) 
        		playerRackButtons[i].setVisible(visible);
    	}
    }
    
    private final ActionListener showHideRack_Listener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {

        	showHideRack();
        }
    };
    
    private static void seeWhoStarts() {

    	// see which button was pressed - 1 or 2 player:
    	String str1 = new String("");

        printIf("PICKING-LETTERS: "+pickingLetters);
        if (pickingLetters==0) {
        	// ################### FIRST DIALOGUE ASKING TO PICK A LETTER
        	// 1P & 2P GAMES WILL NEED THIS
        	
            //if (number == 2) {
        	if (twoPlayerGame) {
        			db_textArea[5].setText("TWO-PLAYER GAME:");
	            	db_textArea[0].setText("Player 1 - click to choose a letter...");
            } else {
    			db_textArea[5].setText("ONE-PLAYER GAME:");
            	db_textArea[0].setText("Click to choose a letter...");
            }
        pickRandomLetter.setVisible(true);
        welcomeScreen.setVisible(false);
        jumbling = true;
        
        showLetterChange(); // starts the random letter timer - stop at #2
        	
        if (twoPlayerGame)
        	pickingLetters = 1;
        else
        	pickingLetters = 2; // 1 player game, skip #1
        	
        } else if (pickingLetters==1) { // PROMPT P2 to PICK ALSO
        	
        	pickedLetters[0] = pouch[1]; // pick the next letter in pouch, after 0 index
    		jumblePouch();
        	
    		str1 = twoPlayerGame ? "Player 1" : "You";
    		str1 = str1 + " picked: '" + getLetter(pickedLetters[0]) + "'";
    		db_textArea[5].setText(str1);
    		
    		db_textArea[0].setText("Player 2, pick a letter...");
        	
    		pickingLetters++;
    		
        } else if (pickingLetters==2) {
        	
        	// stop the random letter display
        	timer.stop();
        	
        	// pick a piece:
    		pickedLetters[1] = pouch[1];
    		jumblePouch();
        	
        	// if 1P game - pick computer's piece:
        	if (!twoPlayerGame) {
        		pickedLetters[0] = pouch[1];
        		jumblePouch();
        	}
        	
        	// BOTH LETTERS ARE PICKED - DISPLAY RESULTS:
        	
        	// IF THERE'S A TIE - REPEAT, i.e., back to step #0
        	if (pickedLetters[0]==pickedLetters[1]) {
        		pickingLetters = 0; // REPEAT
        		db_textArea[5].setText("It's a TIE! Both picked '" + getLetter(pickedLetters[0]) + "'.");
        		db_textArea[0].setText("Let's try again...");
        		db_textArea[3].setText("");
        	} else {
        		str1 = twoPlayerGame ? "Player 1" : "You";
        		str1 = str1 + " picked: '" + getLetter(pickedLetters[0]) + "', ";
        		str1 += twoPlayerGame ? "Player 2" : "I";
        		str1 = str1 + " picked: '" + getLetter(pickedLetters[1]) + "'";
        		db_textArea[5].setText(str1);
        		
        		if ((pickedLetters[0]==26) 
        				|| ((pickedLetters[0] < pickedLetters[1]) 
        					&& (pickedLetters[1]!=26))
        			) { // PLAYER 1 WINS
            			str1 = twoPlayerGame ? "Player 1 goes" : "You go";
            			
            			currentPlayer=0;
            			refreshRack();
        			} else { // PLAYER 2 WINS
            			str1 = twoPlayerGame ? "Player 2 goes" : "I go";
            			
            			currentPlayer=1; // (0,1) == (1,2) for players here
        			}
    			str1 = str1 + " first.";
        		db_textArea[0].setText(str1);
        		db_textArea[3].setText("");
        		db_button[0].setText("Click to start Scrabble...");
            	
            	// COMPARE PIECES - DISPLAY WINNER:
        		
        		pickingLetters++;
        	}
        	
        	
        } else if (pickingLetters==3) {
        	
// INDEX 3 - START SCRABBLE GAME:
    	
        //open Scrabble
        
    	 scrabbleBoardWindow.setVisible(true);
    	 scrabbleBoardWindow.setAlwaysOnTop(true);
         pickRandomLetter.setVisible(false);
         disableBoard = false;
         
         if (twoPlayerGame) {
            
         	if (currentPlayer==1) { // currentPlayer is (0,1) for player (1,2)
            	playerTile.setText("Player 2");
        		 topDisplay.setText("Player 2 goes first...");
            	refreshRack();
            } else {
       		 topDisplay.setText("Player 1 goes first...");
       		 refreshRack();
            }
         	
         // hide the rack so the other player doesn't see
        	if (playerRackButtons[0].isVisible())
        		showHideRack();
         	
         } else { // 1-PLAYER GAME:
        	 
          	// no need to hide pieces if only 1 player
         	 showHideButton.setVisible(false);
    
         	if (!playerRackButtons[0].isVisible())
         		showHideRack();
         	 // test Computer move
         	 if (currentPlayer==1) {
         		 topDisplay.setText("The Computer goes first...");
         		 playerTile.setText("Computer");
         		currentPlayer = 0;
           		 refreshRack(); 
           		currentPlayer = 1;
         		 // DON'T THINK I NEED: refreshRack();
         		currentPlayer=0;
         		 computersMove();
         	 } else {
         		 topDisplay.setText("You go first...");
         		 currentPlayer=0;
         	 }
         } // 1-PLAYER GAME
        	
        	
        } // INDEX 3
    
    } // see who starts
    
    private final static ActionListener pick1or2Players_Listener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {

            JComponent source = (JComponent)e.getSource();
            Integer number = (Integer) source.getClientProperty(ID_PROPERTY);
            if (number == 2)
            	twoPlayerGame = true;
            else 
            	twoPlayerGame = false;
            seeWhoStarts();
            
        }
    };
    
    private final static ActionListener goScrabble_Listener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	seeWhoStarts();
        } // actionPerformed   
    }; // goScrabble_Listener


    static Timer timer = new Timer(500, new ActionListener() { 
        char curLetter; 
        public void actionPerformed(ActionEvent e) {
        	if (pickingLetters>2) {
        		timer.stop();
        	} else {
	        	jumblePouch();
	        	curLetter = getLetter(pouch[0]);
	        	   db_textArea[3].setText("Letter: '" + curLetter + "'");
	        	}
        }
    });
    
    static Timer welcomeIntro = new Timer(200, new ActionListener() { 

        int i;
        public void actionPerformed(ActionEvent e) {
        
        	if (welcome_index == 0) {
        		byte[] welcome1 = picker(7,7);
        		byte[] welcome2 = picker(2,2);
        		byte[] welcome3 = picker(8,8);
        		for (i=0; i<7; i++)
        			welcome_jumble[i] = welcome_text[welcome1[i]];
        		for (i=0; i<2; i++)
        			welcome_jumble[i+7] = welcome_text[7+welcome2[i]];
        		for (i=0; i<8; i++)
        			welcome_jumble[i+9] = welcome_text[9+welcome3[i]];
        	}
        	
        	if (welcome_index < 17) {

        		placePiece(welcome_place[welcome_index], welcome_jumble[welcome_index], getCode(welcome_jumble[welcome_index]));
        		


        		
            } else if (welcome_index == 17){            	
        		for (i=0; i<welcome_text.length; i++) {
            		placePiece(welcome_place[i], welcome_text[i], getCode(welcome_text[i]));
            	}

            } else if (welcome_index == 25) {
            	buttonReplaceTiles.setVisible(true);
            	buttonPlay.setVisible(true);
            	buttonUndo.setVisible(true);
            	buttonExit.setVisible(true);
            	buttonSkip.setVisible(true);
            	showHideButton.setVisible(true);
            	welcomeScreen.setVisible(true);
            	welcomeScreen.setAlwaysOnTop(true); 
                scrabbleBoardWindow.setVisible(false);
                playerTile.setText("Player 1");
                disableBoard = false;
                showHideRack();
            	welcomeIntro.stop();    
                
            	for (int i=0; i<225; i++) { 
                	scrabbleBoard[i] = ' ';
                	setClearTile(i);
            	}
            }
        	
    		// increment the index:
        	welcome_index++;
        }
    });
    
    // this method "animates" out the move that the computer has just made
    // it uses a global (cp_move_index) variable in a loop
    
    static Timer computerMove = new Timer(1000, new ActionListener() { 
        char c; 
        public void actionPerformed(ActionEvent e) {
        	
        	// safeguard:
        	if (movesCount==0)
        		computerMove.stop();
        	
        	if (cp_move_index < movesCount) {
        		c = computerTileChosen[cp_move_index];
        		placePiece(moves[7+cp_move_index], c, getCode(c));
        		
        		scrabbleBoard[moves[7+cp_move_index]] = computerTileChosen[cp_move_index];

        		scrabbleBoard_buttons[moves[7+cp_move_index]].setOpaque(true);
        		scrabbleBoard_buttons[moves[7+cp_move_index]].setBackground(Color.CYAN);
        		
        		
        		
        		// increment the index:
        		cp_move_index++;
            } else {            	
            	for (int i=0; i<movesCount; i++) {
            		scrabbleBoard_buttons[moves[7+i]].setOpaque(false);
            		scrabbleBoard_buttons[moves[7+i]].setBackground(null);            		
            	}

	        	totalScore[1]+=save_cpScore;
	        	skipCount[1]=0;
	        	scoreBoard[1].setText(""+totalScore[1]);
	        	bottomDisplay.setText("Computer SCORED "+save_cpScore+"; "+wordsStatus);		            
	            movesCount=0;
	            disableBoard = false;	// allows the player to "press things"
        		playerTile.setText("Player 1");
	        	computerMove.stop();
	        	
	        	// refill computer's rack:
	        	
        		fillPieces(1, true); 
            }
        	
        }
    });

    
    
    private static void showLetterChange() {
        	jumblePouch();
        	timer.start();

    }
    
    private final static ActionListener pickLetter_Listener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {

        	// see which button was pressed - 1 or 2 player:
        	
            JComponent source = (JComponent)e.getSource();
            Integer number = (Integer) source.getClientProperty(ID_PROPERTY);

        	// open Scrabble
        	 welcomeScreen.setVisible(false);
        	 scrabbleBoardWindow.setVisible(true);
        	 scrabbleBoardWindow.setAlwaysOnTop(true); 
             if (number == 2) {
             	twoPlayerGame = true;
             	showHideRack();	// hide the rack so the other player doesn't see
             }
        }
        
        
    };

    
    public static void setClearTile(int i3) {
        String bText = new String("");
      	Color bCol;

		if (i3==112) {
    		bText = "<html><span style=\"font-size: 2.2em\">"+((char)9733)+"</span><br /><span style=\"font-size: 0.8em\">START</span></html>";        			
    		bCol = Color.RED;
		} else if (scrabbleBoard_special[i3]==1) {
    		bText = "<html><span style=\"font-size: 0.8em\">DOUBLE LETTER SCORE</span></html>";        			
    		bCol = new Color(51,204,255);
		} else if (scrabbleBoard_special[i3]==2) {
    		bText = "<html><span style=\"font-size: 0.8em\">TRIPLE LETTER SCORE</span></html>";        			
    		bCol = new Color(51,153,255);
		} else if (scrabbleBoard_special[i3]==3) {
    		bText = "<html><span style=\"font-size: 0.8em\">DOUBLE WORD SCORE</span></html>";        			
    		bCol = Color.orange;
		} else if (scrabbleBoard_special[i3]==4) {
    		bText = "<html><span style=\"font-size: 0.8em\">TRIPLE WORD SCORE</span></html>";        			
    		bCol = Color.pink;
		} else {
    		bText = " ";        			
    		bCol = new Color(0, 102, 0); // or 0, 153, 0
		}

        scrabbleBoard_buttons[i3].setOpaque(true);
        scrabbleBoard_buttons[i3].setBackground(bCol);
        scrabbleBoard_buttons[i3].setText(bText);
    } // setClearTile()

    
    public JPanel buildscrabblePanel() {
    	int i,ii,i3;
  
        JPanel panel = new JPanel();
   
        panel.setLayout(new GridLayout(16, 15));
        for (i=0; i<15; i++) {
        	for (ii=0; ii<15; ii++) {
        		i3 = (i*15)+ii;
            	scrabbleBoard_buttons[i3] = new JButton("");
                scrabbleBoard_buttons[i3].putClientProperty(ID_PROPERTY, i3);
                scrabbleBoard_buttons[i3].addActionListener(scrabbleBoard_listener);
        		setClearTile(i3);
        		
        		panel.add(scrabbleBoard_buttons[(i*15)+ii]);
        	}
        }
        
        playerTile = new JLabel(" ");
        panel.add(playerTile);
                
        JLabel blankTile = new JLabel(" ");
        panel.add(blankTile);
        
        
        for (ii=0; ii<7; ii++) {
        	playerRackButtons[ii] = new JButton(showButton(pieces[currentPlayer][ii], ((int)pieces[currentPlayer][ii])-65));
        	playerRackButtons[ii].putClientProperty(ID_PROPERTY_2,pieces[currentPlayer][ii]);
        	playerRackButtons[ii].putClientProperty(ID_PROPERTY,ii);
        	playerRackButtons[ii].addActionListener(pickFromRack_listener);
    		panel.add(playerRackButtons[ii]);
        }
        //
        JLabel blankTile2 = new JLabel(" ");
        //panel.add(blankTile2);
        
        showHideButton = new JButton(showHide[0]);
        showHideButton.addActionListener(showHideRack_Listener);
        panel.add(showHideButton);
        
        buttonPlay = new JButton("PLAY");
        buttonPlay.addActionListener(playListener);
        panel.add(buttonPlay);
        
        buttonUndo = new JButton("UNDO");
        buttonUndo.addActionListener(undoListener);
        panel.add(buttonUndo);
        
        buttonReplaceTiles = new JButton("SWAP");
        buttonReplaceTiles.addActionListener(replaceTilesListener);
        panel.add(buttonReplaceTiles);

        buttonSkip = new JButton("SKIP");
        buttonSkip.addActionListener(skipGo_listener);
        panel.add(buttonSkip);
        
        buttonExit = new JButton("EXIT");
        buttonExit.setMnemonic(KeyEvent.VK_C);
        buttonExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        panel.add(buttonExit);
        
        return panel;

    }

    public JPanel buildsidebar() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));
        for (int i=0; i<2; i++) {
            scoreBoard[i] = new JLabel("0");
            scoreBoard[i].setHorizontalAlignment(SwingConstants.CENTER);
            scoreBoard[i].setPreferredSize(new Dimension(40, 25));
            scoreBoard[i].setFont(new Font("Dialog", Font.BOLD, 18));
            panel.add(scoreBoard[i]);
        }
        return panel;
    }

    public JPanel buildClearPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3));

        return panel;
    }
    
    public static String showValue(char pickedPiece) {
    	String retStr = new String("");
    	int i;
    	i=((int)pickedPiece)-65;
    	if (i>-1 && i<letterValues.length) {
    		i = letterValues[i];
    		retStr=""+i;
    	} else {
    		retStr="WILDCARD "+i+";";
    	}
    	return retStr;
    }
    
    public static String showButton(char pickedPiece, int pickedVal) {
    	String chrStr = new String("");
    	String valStr = new String("");
    	int i = pickedVal;

    	if (pickedPiece == '-') {
    		chrStr="-";
    		valStr="";
    	} else if (pickedPiece == ' ') {
    		chrStr="";
    		valStr="WILDCARD";
    	} else {
    		chrStr=""+pickedPiece;
        	if (i>-1 && i<letterValues.length) {
        		i = letterValues[i];
        		valStr=""+i;
        	} else 
        		valStr="0";
    	} // END: not Wildcard or blank '-'    	


    	String retStr = new String("<html><span style=\"font-size: 1.8em\">"+chrStr+"</span><br /><span style=\"font-size: 0.8em\">"+valStr+"</span></html>");
    	return retStr;
    }
    
    public static void wcButtonPressed(char n) {
    	
    	wildcardWindow.setVisible(false);
    	
    	// set wildcard to n:
    	pickedPiece = n;
    	pieces[currentPlayer][pickedPieceNum]=n;
    	printIf("SB PRESSED: "+n+ ": "+pickedPieceNum);

    	addToBoard(lastSquarePressed,-1);
    	
    }
    
    // // / wcSubmitButtonListener
    private final static ActionListener wcButtonListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            JComponent source = (JComponent) e.getSource();
            char opCode = (char) source.getClientProperty(WILDCARD_PROPERTY);
            wcButtonPressed(opCode);
        }
    };
    
    public static void openWildcardWindow() {

    	wildcardWindow.setVisible(true);
    	wildcardWindow.setAlwaysOnTop(true);
    }
    
    public static void placePiece(int i, char pickedPiece, int n) {
        String bText = new String("");
    	bText = showButton(pickedPiece,n); 
        scrabbleBoard_buttons[i].setText(bText);
        scrabbleBoard_buttons[i].setOpaque(false);
        scrabbleBoard_buttons[i].setBackground(null);
    }
    
    public static void addToBoard(int i, int n) {

        
        if (n==-1) {
        	pickedPiece+=32;
        }
    	printIf("::::" + n);
    	placePiece(i, pickedPiece,n);
        
        scrabbleBoard[i] = pickedPiece;
        playerRackButtons[pickedPieceNum].putClientProperty(ID_PROPERTY_2,'-');
        playerRackButtons[pickedPieceNum].setText("-");

        pieces[currentPlayer][pickedPieceNum] = '-';
        
        // remember this move in case it must be undone
        if (movesCount<7) { // can't have more than 7 moves
        	movesCount++;
        	moves[movesCount-1]=pickedPieceNum;
        	moves[movesCount+6]=i;
        }
        printIf(""+movesCount);
        
        pickedPieceNum=-1;
    }

    public static void scrabbleBoardPressed(JComponent source, int i) {
        String displayText = bottomDisplay.getText();
        String valueString = Integer.toString(i);
 
        //System.out.println(source.getClientProperty(ID_PROPERTY));
        //source.putClientProperty(ID_PROPERTY, 0);
        if (scrabbleBoard[i]!=' ') {
        	// THERE ALREADY IS A TILE ON THAT SQUARE
        	topDisplay.setText("THERE ALREADY IS A TILE ON THAT SQUARE");
        	printIf("ALREADY TAKEN");
        } else {
        	topDisplay.setText(" ");
	        if ((pickedPieceNum>=0) && (pickedPieceNum<7)) {
	            if (pickedPiece == ' ') {
	            	printIf("WILDCARD!!!");
	            	openWildcardWindow();
	            	//wcData[0]=pickedPiece;
	            	//wcData[1]=pickedPieceNum;
	            	lastSquarePressed = i;
	            	// // //
	            } else {
	            	// add piece to board - replace rack afterwards if available
	            	printIf("::::" + (((int)i)-94));
	            	addToBoard(i, ((int)pickedPiece)-65);
	            }
	        }
        }
        bottomDisplay.setText(displayText);
    }

    
    public static void undoButtonPressed(boolean isComputer) {
    	
    	// undo MOVES
    	int i;
    	char c;
    	String temp = new String();
    	
    	for (i=0; i<movesCount; i++){  
    		printIf("UNDO:" + i + ". " + moves[i] + " - " + moves[i+7]);
    		printIf(scrabbleBoard[moves[i+7]] + " " + getValue(scrabbleBoard[moves[i+7]]));
    		// ababa
    		if (!isComputer) {
    			//printIf("UD:" + moves[i]);
    			temp = scrabbleBoard_buttons[moves[i+7]].getText();
    			pieces[currentPlayer][moves[i]] = scrabbleBoard[moves[i+7]]; // ///
    			printIf("UNDO-: '" + playerRackButtons[moves[i]].getClientProperty(ID_PROPERTY_2)+"'");
        		
    			c = scrabbleBoard[moves[i+7]];

    			
    					//(char)playerRackButtons[moves[i]].getClientProperty(ID_PROPERTY_2);
        		// THE ID ON THE BUTTON WAS USED
        		// NOW ITS NOT THERE!

    			printIf(""+c+ "; VAL:"+getValue(c));
    			if (getValue(c)<0) {
//        		if (c==' ') {
        			temp = showButton(' ',-1);
        			pieces[currentPlayer][moves[i]] = ' ';
        			printIf("WC UNDO");
        		}
    			playerRackButtons[moves[i]].setText(temp);
    		
			setClearTile(moves[i+7]);
    		}
			scrabbleBoard[moves[i+7]] = ' ';
    	}
    	if (!isComputer)
    		movesCount=0;
    }
    
    public static int checkForError(int x1, int y1, int x2,int y2, boolean onStar) {
    	int errorFound = -1;

      	// play MOVES
    	boolean legalMove = true;
    	boolean touching = false;
    	boolean gapFound = false;

      	int tempY, i;
      	
      	// shouldn't be out of bounds, but just in case:
      	
      	if (x1<0 || x2>14 || y1<0 || y2>14) {
      		 printIf("OUT OF BOUNDS ERROR");
      		return 8;
      	}

        if (firstPlay) {
       	 printIf("First PLAY");
      
       	 if (movesCount<2) {  // you must place at least 2 tiles on the first move.
       		 if (onStar)
       			 return 6;
       		 else
       			 return 7;
       	 }
       	 
         if (onStar) {
           	 printIf("ON A STAR");
           } else {
        	   printIf("ILLEGAL MOVE");
        	   return 1;
        	   
           }
       }
        if (movesCount>1) {
	        if ((x1!=x2) && (y1!=y2)) {
	        	return 2; // diagonal illegal placement
	        } else if (x1!=x2) {
	         	   printIf("Horizontal move: " + x1 + " - " + x2);
		         	  for (i=x1; i<=x2; i++) {
		        			if (scrabbleBoard[(y1*15)+i]==' ') {
			        			printIf("You can't leave gaps between placed tiles!");
			        			gapFound = true;
			        			return 4;
			        		} 
	  				   }
		         	  // no gap found so if (x2-x1+1) > movesCount
		         	  // there must be existing pieces in between
		         	 if ((x2-x1+1)>movesCount) {
		         		 printIf("PIECES IN BETWEEN TOUCHING");
		         		 touching = true;
		         	 }
		         	  
		         	  if (!firstPlay && !touching) {	         	   
		        		// walk along the word. 
		        		// if nothing touches ends or any of the top/bottoms, error
		        		if (x1>0 && scrabbleBoard[(y1*15)+x1-1]!=' ') {
		        			printIf("touching start");
		        			touching = true;
		        		} else {
		        			if (x2<14 && scrabbleBoard[(y1*15)+x2+1]!=' ') {
			        			printIf("touching end");
			        			touching = true;
			        		} else {
			        			// check top and bottom for a touch
			        			if (y1>0) { // check top
			        				tempY = (y1-1)*15;
			        				for (i=x1; i<=x2; i++) {
			    	        			if (scrabbleBoard[tempY+i]!=' ') {
			    		        			printIf("touching top");
			    		        			touching = true;
			    		        			break;
			    		        		} 
			        				}
			        			}
			        			if (!touching && y1<14) { // check bottom
			        				tempY = (y1+1)*15;
			        				for (i=x1; i<=x2; i++) {
			    	        			if (scrabbleBoard[tempY+i]!=' ') {
			    		        			printIf("touching bottom");
			    		        			touching = true;
			    		        			break;
			    		        		} 
			        				}
			        			}
			        			
			        		}
		        		} // check touching
	        		} // ! firstPlay - check - must touch existing tiles
	        	} else {
	         	   printIf("Vertical move");
	         	  for (i=y1; i<=y2; i++) {
	        			if (scrabbleBoard[(i*15)+x1]==' ') {
		        			printIf("You can't leave gaps between placed tiles!");
		        			gapFound = true;
		        			return 4;
		        		} 
  				   }
	         	  
	         	  // no gap found so if (y2-y1+1) > movesCount
	         	  // there must be existing pieces in between
	         	 if ((y2-y1+1)>movesCount) {
	         		 printIf("PIECES IN BETWEEN TOUCHING");
	         		 touching = true;
	         	 }
	         	  
	         	  if (!firstPlay && !touching) {
	         	   
	        		// walk down the word.
	        		// if nothing touches ends or any of the top/bottoms, error
	        		if (y1>0 && scrabbleBoard[((y1-1)*15)+x1]!=' ') {
	        			printIf("touching top");
	        			touching = true;
	        		} else {
	        			if (y2<14 && scrabbleBoard[((y2+1)*15)+x1]!=' ') {
		        			printIf("touching bottom");
		        			touching = true;
		        		} else {
		        			// check left and right for a touch
		        			if (x1>0) { // check left
		        				System.out.print("CHECK LEFT: "+y1+","+y2);
		        				for (i=y1; i<=y2; i++) {
		        					System.out.print(scrabbleBoard[(i*15)+x1]+ " ");
		    	        			if (scrabbleBoard[(i*15)+x1-1]!=' ') {
		    		        			printIf("touching left");
		    		        			touching = true;
		    		        			break;
		    		        		} 
		        				}
		        			}
		        			if (!touching && x1<14) { // check right
		        				for (i=y1; i<=y2; i++) {
		        					if (scrabbleBoard[(i*15)+x1+1]!=' ') {
		    		        			printIf("touching right");
		    		        			touching = true;
		    		        			break;
		    		        		} 
		        				}
		        			}
		        			
		        		}
	        		}
	        	} // ! first play - check - must be touching existing tiles
	        } // vertical move
        } else {
      	   printIf("Single tile move "+x1 + ", "+y1);
      	    // check n/s/e/w for a touching tile
      	   if (x1>0 && scrabbleBoard[(y1*15)+x1-1]!=' ') {
   			printIf("touching left");
   			touching = true;
   			// TO DO TO DO TO DO index -15
      	   } else if (x1<14 && scrabbleBoard[(y1*15)+x1+1]!=' ') {
      			printIf("touching right");
       			touching = true;
           } else if (y1>0 && scrabbleBoard[((y1-1)*15)+x1]!=' ') {
      			printIf("touching top");
       			touching = true;
           } else if (y1<14 && scrabbleBoard[((y1+1)*15)+x1]!=' ') {
      			printIf("touching bottom");
       			touching = true;
           } 
        }
        if (!firstPlay && !touching) {
        	return 3;
        }

        if (legalMove) { // keep checking if no errors found so far
        	// horizontal - walk across for gaps
        	// vertical - walk down for gaps
        	// single tile = no need to do anything in this regard
        	
        }    	
    	
    	
    	return -1;
    }
    
    public static void addWord(String word, int score, int x1, int y1, int x2, int y2) {
    	if  (scrabblewordCount<10) {
    		scrabblewordCount++;
    		wordsList[scrabblewordCount-1].setVars(word,score,x1,y1,x2,y2);
    	}
    	printIf("ADD WORD: "+scrabblewordCount+ " : "+word+", "+score);

    }
    
    public static int checkBonus(int currentPiece) {

    	for (int i=0; i<movesCount; i++){
    		printIf("CB: "+i + ": " + currentPiece + ", " + moves[i+7]+ " -- " + scrabbleBoard_special[currentPiece]);
    		if (currentPiece == moves[i+7]) {
    			return scrabbleBoard_special[currentPiece]; // a bonus is applied
    		}
    	}
    	return 0;
    }
    
    public static String checkHorizontal(int x1, int x2, int y1) {
		// check horizontal x1
    	printIf("CH:" + x1 + ", " + x2 + "; " + y1);
    	int curVal;
    	char curChar;
    	
    	int score = 0;
    	int multiples = 1;
    	
      	String word1 = new String("");
      	int x_1, x_2, i, b, addedScore,currentPiece;

      	
		x_1 = x1;
		while ((x_1>0) && (scrabbleBoard[(y1*15)+(x_1-1)]!=' ')) {
			x_1--;
		}
		x_2 = x1;
		while ((x_2<14) && (scrabbleBoard[(y1*15)+(x_2+1)]!=' ')) {
			x_2++;
		}    	
		
		if (x_1==x_2)
			return ""; // single letter words are excluded
		
		for (i=x_1; i<=x_2; i++) {			
			currentPiece = (y1*15)+i;
			curChar = scrabbleBoard[currentPiece];
			curVal = getValue(curChar);
			b = checkBonus(currentPiece); // only bonus if this is one of our new moves
			if (b==3)
				multiples *= 2;
			else if (b==4)
				multiples *= 3;
			printIf("MULTIPLES:"+multiples);
			
			//FIXSCORE
			// FOR LOWER CASE - WILDCARD - NO SCORE ADDED
			
			if (curVal>0) {
				addedScore = curVal;
				if (b==1)
					addedScore *= 2;
				else if (b==2)
					addedScore *= 3;				
				score+= addedScore;
			}
			//System.out.println("SCORE:"+score+"; MULTIPLES:"+multiples+" = "+(score * multiples));


			word1= word1 + curChar;
			
		} // END: loop through word
		
		score = score * multiples;
		if (word1.length()>1) 
			addWord(word1,score,x1,y1,x2,y1);
		printIf("Score: " + score+" multiples:" + multiples); // need to return this also
		return word1;
    }
    
    public static int workoutLetterBonus(int x, int y) {
    	int score = 0;
    	int scoreType = scrabbleBoard_special[(y*15)+x];
    	char curChar;
    	int curVal;
    	
    	if (scoreType==0)
    		return 0;
    	else {
    		curChar = scrabbleBoard[(y*15)+x];
    		curVal = getValue(curChar);
    		
    		if (scoreType == 1)
    				score = curVal; // DOUBLE LETTER
    			else if (scoreType == 2)
    				score = (curVal * 2); // TRIPLE LETTER
    	}
    	return score;

    }
    
    
    public static String checkVertical(int x1, int y1, int y2) {
    	char curChar;
    	int score = 0;
    	int currentPiece;
    	int curVal;
    	
		// check vertical y1
    	
      	String word1 = new String("");
      	int y_1, y_2, i, b;
      	int addedScore;
      	int multiples = 1;
      	
		y_1 = y1;
		while ((y_1>0) && (scrabbleBoard[((y_1-1)*15)+x1]!=' ')) {
			y_1--;
		}
		y_2 = y1;
		while ((y_2<14) && (scrabbleBoard[((y_2+1)*15)+x1]!=' ')) {
			y_2++;
		}    		
		if (y_1==y_2)
			return ""; // single letter words are excluded
		
		for (i=y_1; i<=y_2; i++) {
			currentPiece = (i*15)+x1;
			curChar = scrabbleBoard[currentPiece];
    		curVal = getValue(curChar);
    		
			b = checkBonus(currentPiece); // only bonus if this is one of our new moves
			
			if (b==3)
				multiples *= 2;
			else if (b==4)
				multiples *= 3;
			
			 // LOWER CASE - WILDCARD - NO SCORE ADDED
			if (curVal>0) {
				addedScore = curVal;
				if (b==1)
					addedScore *= 2;
				else if (b==2)
					addedScore *= 3;
				score+= addedScore;
			}
			word1= word1 + curChar;
		}
		score = score * multiples;
		
		if (word1.length()>1) 
			addWord(word1,score,x1,y1,x1,y2);
		printIf("Score: " + score); // need to return this also

		return word1;
    }
    
    public static int findIn(int target, int[] array,int len) {
    	for (int i=0; i<len; i++) {
    		if (array[i] == target)
    			return i;
    	}
    	return -1;
    }
    
    
    // COMPUTER PICKS TILES FROM HIS POUCH:
    
	public static byte[] picker(int potSize, int numbersToPick) {

		Random r = new Random();
		byte[] returnVals = new byte[numbersToPick];
		
		byte numbersPicked = 0;
		byte cp = 0;
		byte currentNumber = 0;
		byte[] numbers = {0,1,2,3,4,5,6,7};

		while (numbersToPick>0) {
				cp=(byte)r.nextInt(potSize);
				currentNumber = numbers[cp];
				if (cp!=(potSize-1)) {
					// switch with last
					numbers[cp] = numbers[potSize-1];
				}
				potSize--;
				numbersToPick--;
			//System.out.println(numbersPicked + ". " + currentNumber+" ");
			returnVals[numbersPicked]=currentNumber;
			numbersPicked++;
		}
		
		return returnVals;
		
	} // letterPicker
	
	// replaces tile that the CP used in its move with one from the pouch
	
	public static void replaceComputersTile() {
		
	}
    
    
    public static void computersMove() {
    	
    	// Disable buttons until computer move has completed
    	disableBoard = true; // TO-DO: this should go in a little earlier
    	
    	temp_inc ++;
    	
    	int[] firstFound = new int[200]; // temp: potentially more could be found

    	boolean computerScored = false;
    	int foundCount=0;
    	int n;
    	int i,ii;
    	
    	if (firstPlay) { // if the computer goes first start on the star
    		firstFound[0] = 112;
			foundCount = 1;
    	} else {    	 // if it is not the first move
    		
	    	for (i = 0; i<scrabbleBoard.length; i++) { // loop to check for placed pieces
	    		if (scrabbleBoard[i]!=' ') {

	    			// check north;
	    			if (i>14 && scrabbleBoard[i-15] ==' ') {
	    				n = i-15;
	
	    				if (findIn(n, firstFound, foundCount)==-1) { // eliminate duplicates
	    					firstFound[foundCount] = n;
	    					foundCount++;

	    				}
	    			}
	    			// check south
	    			if (i<210 && scrabbleBoard[i+15] ==' ') {
	    				n = i+15;
	
	    				if (findIn(n, firstFound, foundCount)==-1) { // eliminate duplicates
	    					firstFound[foundCount] = n;
	    					foundCount++;

	    				}
	
	    			}
	    			// check east
	    			if ((i+1)%15 !=0 && scrabbleBoard[i+1] ==' ') {
	    				n = i+1;
	
	    				if (findIn(n, firstFound, foundCount)==-1) { // eliminate duplicates
	    					firstFound[foundCount] = n;
	    					foundCount++;

	    				}
	
	    			}
	    			// check west
	    			if (i%15 !=0 && scrabbleBoard[i-1] ==' ') {
	    				n = i-1;
	
	    				if (findIn(n, firstFound, foundCount)==-1) { // eliminate duplicates
	    					firstFound[foundCount] = n;
	    					foundCount++;

	    				}
	
	    			}
	    		}
	    		//System.out.println("FIRSTFOUND: " + firstFound[foundCount-1]);
	    		
	    	} // loop to check for placed pieces
    	
    	} // if it is not the first move
    	
    	
    	
    	int possiblePlacements = 0;
    	int[][] placements = new int[4010][8]; // use linklist
    	// there could be more than 2000 placements as occured in testing
    	
    	// TO-DO: ENSURE NO OVERFLOW
    	
    	for (int movesLoop=0; movesLoop<foundCount; movesLoop++) {
    	
    	int[] spacesH = new int[13];
    	int[] spacesV = new int[13];
    	int[] sP = new int[4];
    	
    	for (i=0; i<13; i++) {
    		spacesH[i]=-1;
    		spacesV[i]=-1;
    	}
    	
    	// the next part you will loop 0 -> foundCount-1; remove tempInc
    	
    	// try potential NEW moves with this first square
    	
    	// get north
    	int north2 = firstFound[movesLoop];
    	int north1 = firstFound[movesLoop];
    	spacesV[6]= north2; // 6 is the centre piece with MAX 6 either side
    	sP[0]=6; 
    	
    	int piecesCount_N = 1;
    	while (piecesCount_N<7 && north1>14) {
    		north1 -= 15;
    		if (scrabbleBoard[north1] ==' ') {
    			piecesCount_N++;

    			sP[0]--;
    			spacesV[sP[0]]= north1;
    		}
    	}
    	
    	// get south
    	int south1 = firstFound[movesLoop];
    	int south2 = firstFound[movesLoop];
    	int piecesCount_S = 1;
    	sP[1]=6; 
    	
    	while (piecesCount_S<7 && south2<210) {
    		south2 += 15;
    		if (scrabbleBoard[south2] ==' ') {
    			piecesCount_S++;

    			sP[1]++;
    			spacesV[sP[1]] = south2;
    		}
    	}
    		
        	
        	
    	// get east
    	int east1 = firstFound[movesLoop];
    	int east2 = firstFound[movesLoop];
    	int piecesCount_E = 1;
    	
    	sP[3]=6; 
        spacesH[6] = east2; // 6 is the centre piece with MAX 6 either side

    	
    	while (piecesCount_E<7 && (east2+1)%15 !=0) {
    		east2 += 1;
    		if (scrabbleBoard[east2] ==' ') {
    			piecesCount_E++;

    			
    			sP[3]++;
    			spacesH[sP[3]] = east2;
    		}
    	}
    	
    	// get west
    	
    	sP[2]=6;
    	
    	int west2 = firstFound[movesLoop];
    	int west1 = firstFound[movesLoop];
    	int piecesCount_W = 1;
    	while (piecesCount_W<7 && (west1 % 15 !=0)) {
    		west1 -= 1;
    		if (scrabbleBoard[west1] ==' ') {
    			piecesCount_W++;
    			
    			sP[2]--;
    			spacesH[sP[2]] = west1;
    		}
    	}
    	
    	System.out.print("SPACES (VERTICAL): ");
    	for (i=0; i<13; i++) {
    		if (spacesV[i] == -1)
    			System.out.print(" -- , ");
    		else
    			System.out.print(spacesV[i] + ", ");
    	}
        	printIf("; ");

        	System.out.print("SPACES (HORIZONTAL): ");
        	for (i=0; i<13; i++) {
        		if (spacesH[i] == -1)
        			System.out.print(" -- , ");
        		else
        			System.out.print(spacesH[i] + ", ");
        	}
            	printIf("; ");
    	
            	int i3;
            	
            	// go through all the possible moves (in terms of word size & placement)

            	placements[possiblePlacements][7]=1;
            	placements[possiblePlacements][0]=spacesV[6];
            	if (possiblePlacements<4000)
            		possiblePlacements++;
            	printIf("SINGLE MOVE: " + spacesV[6]);
            	
            	// computerRackSize = normally 7 but less at end of game
            	
            for (i=2; i<=computerRackSize; i++) { // i = number of letters in word
            	for (ii=0; ii<i; ii++) {
            		// try vertical
            		
            		if (6-ii>=sP[0] && ((5-ii+i)<=sP[1])) {
	            		for (i3 = 0; i3<i; i3++) {
	            			if (i3>0)
	            				System.out.print(", ");
            				n = (6-ii)+i3;
            				moves[i3+7]=n;
            				placements[possiblePlacements][i3] = spacesV[n];
	            			System.out.print("["+i3 + ".v] " + spacesV[n]);
	            			
	            		}
	            		movesCount=i;
	            		placements[possiblePlacements][7] = i;
	            		printIf("["+i + "("+possiblePlacements+")");
	            		if (possiblePlacements<4000)
	            			possiblePlacements++;
            		} else
	            			System.out.print("*** ");
            		if (6-ii>=sP[2] && ((5-ii+i)<=sP[3])) {
	            		for (i3 = 0; i3<i; i3++) {
	            			if (i3>0)
	            				System.out.print(", ");
            				n = (6-ii)+i3;
            				moves[i3+7]=n;
            				placements[possiblePlacements][i3] = spacesH[n];
            				System.out.print("["+i + ".h] " + spacesH[n]);	            			
	            		}
	            		movesCount=i;
	            		placements[possiblePlacements][7] = i;
	            		printIf("{"+i+"("+possiblePlacements+")");
	            		if (possiblePlacements<4000)
	            			possiblePlacements++;
	            		
	            	
	            		/*
	            		 * EG: 80, 95 or 94, 95
	            		 * or 95, 125, 140
	            		 * or 95, 96, 97
	            		 * 
	            		 * These squares must be filled with pieces
	            		 * from the computer's 7-piece rack
	            		 * then tested to see if the words are in dictionary
	            		 * 
	            		 */
	            		
            		} else
	            			System.out.print("*** ");
            		
            		System.out.print(" ; ");
            	}

            } 
            
    	} // movesLoop
    	
            printIf("Possible placements: " + possiblePlacements);
            
            int cpScore = 0;
            
            
         // this is a test placement
            for (int i4=possiblePlacements-1; i4>=0; i4--) { // make test moves
	            
	            Random r = new Random();
	            int chosenMove = r.nextInt(possiblePlacements);
	            movesCount = placements[chosenMove][7];

                byte[] tilesPicked = picker(computerRackSize,movesCount); 
                // computerRackSize - normally 7, but less at end of game
                
                // returns eg [0, 2, 3 ]
                // if these tiles are used then
                // remove these from rack and add 3 more
	            
	            for (i=0; i<movesCount; i++) {
	                moves[7+i]=placements[chosenMove][i];
	                moves[i]=tilesPicked[i];
	                
	                //c[i] = (char)(r.nextInt(26)+65);
	                computerTileChosen[i] = pieces[1][tilesPicked[i]];
	                if (computerTileChosen[i]==' ')
	                	computerTileChosen[i] = (char)(r.nextInt(26)+97); //lowercase - to show wildcard
	                	                
	        		scrabbleBoard[placements[chosenMove][i]] = computerTileChosen[i];
	            }   
	            cpScore = playButtonPressed(true,1);
	            
	            if (cpScore>0) {
	            	// THE COMPUTER HAS SCORED...
      	
	            	printIf("MOVES:"+movesCount+" CHOSEN:"+chosenMove);
	            	for (i=0; i<movesCount; i++) {
	            		//moves[7+i]=placements[chosenMove][i];
	            		// GOHERE
	            		//pieces[1][i]=c[i];
	            		//computerTileChosen[i] = tilesPicked[i];
		                // after score we go moves[0-movescount] and replace tile

	            		// NB: TO-DO:
	            		pieces[1][moves[i]]='-'; // this will be replaced in computerMove()
	            		
		            }
	            	
	            	save_cpScore = cpScore;
	            	
		            // slow this down
	            	cp_move_index = 0;
	            	

	            	computerMove.start();
	            	
	            	// to-do: disable other buttons also while cp makes move
	            	
	            	playerTile.setText("Computer");
	            	


	            	computerScored = true;
	            	
	            	// replace tiles 
	            	
		            break;
	            } // if computer has scored
	            	            
            }
            
            if (!computerScored) {
            	bottomDisplay.setText("The Computer has skipped his go.");
            	skipCount[1]++;
            	if ((skipCount[0]>1) && (skipCount[1]>1)) {
            		endGame(1);
            	}
            	
	            movesCount=0;
	            disableBoard = false;
        		playerTile.setText("Player 1");
            }
          

    } // computersMove()
   
    public static void nextPlayersGo() {
  		if (!twoPlayerGame)
    		computersMove();
    	else {
    		// switch around the current player
        	replaceTilesButtonPressed = false;
        	
        	currentPlayer = (currentPlayer==0) ? 1 : 0; 
        	playerTile.setText("Player "+(currentPlayer+1));
        	refreshRack();
        	// hide the rack so that the other player doesn't see:
        	printIf("BUTTONS: "+scrabbleBoard_buttons[0].isVisible());
        	if (playerRackButtons[0].isVisible())
        		showHideRack();
    	}
    }
    
    public static int playButtonPressed(boolean isComputer, int playerNumber) {
      	String message = new String("");
      	String word1 = new String("");
      	String word2 = new String("");
      	
      	boolean onStar = false;
      	int i,number,tX,tY;
    	int x1 = -1;
    	int x2 = -1;
    	int y1 = -1;
    	int y2 = -1;      	
    	int[] movesX = new int[7];
    	int[] movesY = new int[7];
    	int letterBonus = 0;
    	int score = 0;
    	
      	for (i=0; i<movesCount; i++){
      		number = moves[i+7];
            tY = number / 15;
            tX = number % 15;
            
            movesX[i] = tX;
            movesY[i] = tY;
            
      		if (number == 112) {
      			onStar = true;
      		}
            if (i==0) {
            	x1 = tX;
            	x2 = tX;
            	y1 = tY;
            	y2 = tY;
            } else {
            	if (tX<x1)
            		x1 = tX;
            	if (tX>x2)
            		x2 = tX;
            	if (tY<y1)
            		y1 = tY;            	
            	if (tY>y2)
            		y2 = tY;
            }
            
            printIf(""+number + ". " + tX + ", " + tY);
            }
      	
    	int errorCheck = -1;
    	
    	if (!isComputer) // no need to check for computer
    		errorCheck = checkForError(x1,y1,x2,y2,onStar);
    	
    	
    	if (errorCheck==-1) {
//    		for (i=0; i<movesCount; i++)
//         		printIf("MOVE " + i + ": " + scrabbleBoard[moves[i+7]]);
  
    		if (movesCount==1) {
    			printIf("NO ERROR: SINGLE PIECE MOVE ");
    			// check horizontal
    			word1 = checkHorizontal(x1,x2,y1);
    			printIf("WORD 1:" + word1);
    			// check vertical
    			word2 = checkVertical(x1,y1,y2);
    			printIf("WORD 2:" + word2);
    			// any word here found will receive the bonus of this letter
    			letterBonus = workoutLetterBonus(x1, y1);
				printIf("BONUS:" + letterBonus); 
    		} else {
    			if (y1==y2) {
    				printIf("NO ERROR: HORIZONTAL MOVE");	
        			// check horizontal
        			word1 = checkHorizontal(x1,x2,y1);
        			
        			
        			printIf("WORD 1:" + word1);
        			// check vertical x every word
        	      	for (i=0; i<movesCount; i++){
        	      		// check for horizontal bonuses
        	      		// check for vertical bonuses
        	      		
        	      		// YOU MUST APPLY DOUBLE LETTERS
        	      		// BEFORE YOU APPLY DOUBLE WORDS
        	      		
	        			word2 = checkVertical(movesX[i],y1,y1);
	        			printIf("WORD 2V "+i+":" + word2);
	        			letterBonus = workoutLetterBonus(movesX[i], y1);
	    				printIf("BONUS:" + letterBonus);
        	      	}
    			} else {
    				printIf("NO ERROR: VERTICAL MOVE");
        			// check vertical
        			word1 = checkVertical(x1,y1,y2);
        			printIf("WORD 1:" + word1);
        			// check horizontal x every word
        	      	for (i=0; i<movesCount; i++){
	        			word2 = checkHorizontal(x1,x1,movesY[i]);
	        			printIf("WORD 2H "+i+":" + word2);
	        			letterBonus = workoutLetterBonus(x1, movesY[i]);
	    				printIf("BONUS:" + letterBonus);
        	      	}
    			}
    		} // get words
    		
    		// now check if they are in the dictionary. if any aren't then error, undo move.
    		wordsStatus = "";
    		for (i=0; i<scrabblewordCount; i++) {
    			if (isComputer)
    				System.out.print("CMP, ");
    			System.out.print("CHECK DICTIONARY: " + wordsList[i].word + ", score: "+ wordsList[i].score);
 
    			if (!dictionary.findWordFast(wordsList[i].word.toLowerCase())) {	
    				message = wordsList[i].word + " is not in the dictionary!";	
    				errorCheck = 5;
    	    		scrabblewordCount = 0;
    	    		score = 0;
    				break;
    			} else {
    				wordsStatus+=wordsList[i].word + " ("+ wordsList[i].score+"); ";
    				score += wordsList[i].score;
    			}
    		}
    		if (errorCheck==-1) {
    			if (movesCount == 7) 
    				score += 50; 
    			if (!isComputer) {
    	        	skipCount[playerNumber]=0;
    				totalScore[playerNumber] += score;
	            	scoreBoard[playerNumber].setText(""+totalScore[playerNumber]);
    				topDisplay.setText("Well done you scored " + score + "! "+wordsStatus);
           			fillPieces(currentPlayer, false); // add more pieces to the 7-letter rack
           	 		movesCount = 0;
    			}
    	    	
       	 		firstPlay = false; // only set if it is a legal move
    			scrabblewordCount = 0;	
    		}
      	}
    	if (isComputer)
      		undoButtonPressed(true); // only changes scrabbleBoard array
    	else if (errorCheck>-1) {
      		if (errorCheck == 1)
      	    	message = "For the first move, you need to have one tile on the centre star.";
      		else if (errorCheck == 2)
	        	message = "tiles need to be placed either horizonally or vertically";
      		else if (errorCheck == 3)
        	message = "You need to touch horizontally or vertically at least one existing piece.";
      		else if (errorCheck == 4)
            	message = "You can't leave gaps between placed tiles!";
      		else if (errorCheck == 6)
      			message = "You must place at least 2 tiles on the first move.";
      		else if (errorCheck == 7)
      	    	message = "For the first move, you need to place at least two tiles and have one tile on the centre star.";
      		// error 5 is catered for above
      		topDisplay.setText(message);
      		undoButtonPressed(false);
      	} else {	
      		
      		// human move, no error - if 2pl switch else computer move
        	
      		nextPlayersGo();
      		
      	}

    	return score;

      } // playButtonPressed
  
}