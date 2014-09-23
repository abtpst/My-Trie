/*
 * This Class represents the Nodes that the Trie is made up of. It has the member functions to add and search for words
 * */
package itried;

import java.util.ArrayList;

public class TrieNode implements Comparable<TrieNode> {
	
	// Holds the Char value for this Node
	char value = '\0';
	// Points to the Parent of this Node
	TrieNode parent;
	// This is the list of Chars that follow the current Node in a word that exists in the Trie
	ArrayList<TrieNode> members = new ArrayList<TrieNode>();
	// This flag indicates whether the current Node denotes the end of a word
	boolean isEnd = false;
	// Empty constructor
	TrieNode() {}
	// Constructor
	TrieNode(char c) 
	{
		value = c;
	}
	// This constructor would be useful when we want to signify the end of a word
	TrieNode(char c, boolean f) 
	{
		value = c;
		isEnd = f;
	}
	// This method adds a new word to the Trie
	void addMembers(String str) 
	{
		// First, get the members for this node
		ArrayList<TrieNode> thismem = this.getMembers();
		// Then, create a new TrieNode representing the first character of the word that we want to add
		TrieNode child = new TrieNode(str.charAt(0));
		// Set this TrieNode as the parent of this Char
		child.setParent(this);
		// If this TrieNode has no members yet, then proceed as follows. The new Char would become the first member of this
		// TrieNode
		if (thismem.isEmpty()) 
		{
			// If this word has just a single Char, then we are at the end of the line. Indicate the Child TrieNode created
			// above as the end of a word and add it to the members of this TrieNode. Finally, return.
			if (str.length() == 1) 
			{
				child.setEnd(true);
				thismem.add(child);

				return;
			}

			// Otherwise, add this child to the members of the current TrieNode. Now, recursively call the addMembers
			// function but this time, the child makes the call. Also, the first Char is taken off of the word as we
			// have taken care of it.
			thismem.add(child);
			child.addMembers(str.substring(1));
		}

		// If this TrieNode already has members then proceed as follows
		else 
		{
			// If we have this Char as a member already, then 
			if (thismem.contains(child)) 
			{
				// Check if this is the last Char of the word. If so, then set its isEnd flag and return.
				if (str.length() == 1) 
				{
					thismem.get(thismem.indexOf(child)).setEnd(true);
					return;
				}
				
				// Otherwise, retrieve the Child node representing this Char. Now take off the first Char of this word and 
				// recursively call the addMembers function for the remainder of the word
				thismem.get(thismem.indexOf(child)).addMembers(str.substring(1));
			}

			// If this Char does not yet exist in the members of this TrieNode, then add it
			else 
			{
				// If this word has just a single Char, then we are at the end of the line. Indicate the Child TrieNode created
				// above as the end of a word and add it to the members of this TrieNode. Finally, return.
				if (str.length() == 1) 
				{
					child.setEnd(true);
					thismem.add(child);
					return;
				}

				// Otherwise, add this child to the members of the current TrieNode. Now, recursively call the addMembers
				// function but this time, the child makes the call. Also, the first Char is taken off of the word as we
				// have taken care of it.
				thismem.add(child);
				child.addMembers(str.substring(1));
			}
		}
	}

	// This function is used to search for a word. If the word is not found, we can also add it if the add flag is set
	// at the time the function is called
	void searchNadd(String str, boolean add) 
	{
		// Get the first Char of the word
		char c = str.charAt(0);
		// Get the index of this Char from amongst the members of this TrieNode
		int index = this.getCharIndex(c);

		// If this Char is not amongst the members then,
		if (index == -1) 
		{
			// If the add flag is set, then call the addMembers function for this word and return.
			if (add) 
			{
				this.addMembers(str);
				System.out.println("Word added successfully");
				return;
			}

			// Otherwise, the word does not exist in the Trie so return.
			System.out.println("Not found");
			return;
		}

		// If the index returned above s not -1, we know that the first Char of this word exists in our Trie,
		// so proceed to check the rest of the word.
		else 
		{
			// Get the Child representing this Char
			TrieNode link = this.getMembers().get(index);
			// If the word has just one Char and the link has its isEnd flag set, then we have found the word
			if (str.length() == 1 && link.isEnd()) 
			{
				System.out.println("Found");
				return;
			}
			
			// Otherwise, recursively call the searchNadd function after taking off the first Char from the word as
			// we have already checked for it.
			link.searchNadd(str.substring(1), add);
		}
	}

	// This function searches for a prefix and returns all the words having that prefix. The words would be added to the
	// wordList
	void preFind(String str, String pref, ArrayList<String> wordList) 
	{
		
		// Get the first Char of the word
		char c = str.charAt(0);
		// Get the index of this Char from amongst the members of this TrieNode
		int index = this.getCharIndex(c);

		// If this Char is not amongst the members then the prefix does not exist.
		if (index == -1) 
		{
			System.out.println("Prefix '" + pref + "' does not exist");

			return;
		}

		// Now we know that the first Char of this word exists in our Trie, so proceed to check the rest of the prefix.
		else 
		{
			// Get the Child representing this Char
			TrieNode link = this.getMembers().get(index);
			// If this prefix has just one Char, then we are the end of the line. The prefix that we had set out to look for
			// exists in our Trie. Now we need to add all the words
			if (str.length() == 1) 
			{
				// Call the printChain function for all the members of the current TrieNode
				for (TrieNode l : link.getMembers())
					l.printChain(pref.toString(), wordList);

				return;
			}

			// Otherwise, take off the first Char from the prefix and recursively call the preFind function for 
			// the remaining prefix
			link.preFind(str.substring(1), pref, wordList);
		}
	}

	// This function accumulates all the words with the prefix and adds them to the wordList
	void printChain(String prefix, ArrayList<String> wordList) 
	{
		// If the caller TrieNode denotes the end of a word then,
		if (this.isEnd())
		{
			// If this TrieNode has no members i.e. it is a leaf then form a word by adding the prefix to the Char
			// of this TrieNode. Add this word to the wordList and return
			if (this.getMembers().isEmpty()) 
			{
				wordList.add(prefix + this.getValue());
				return;
			}

			// Otherwise recursively call the printChain function for all the members of this TrieNode. Also, add this
			// TrieNode's Char to the prefix
			for (TrieNode t : this.getMembers())
				t.printChain(prefix + this.getValue(), wordList);
		}

		// Otherwise, this TrieNode does not represent the end of a word, so recursively call the printChain function
		// for all the members of this TrieNode. Also, add this TrieNode's Char to the prefix
		else
			for (TrieNode t : this.getMembers())
				t.printChain(prefix + this.getValue(), wordList);
	}

	// This function returns the index of the member TrieNode that represents the Char being passed
	int getCharIndex(char c) 
	{
		return this.getMembers().indexOf(new TrieNode(c));
	}

	// This function prints out the members of the caller Trienode
	void showMembers() 
	{
		if (this.getMembers().isEmpty())
			return;

		System.out.print("{ ");
		for (TrieNode t : this.getMembers())
			System.out.print(t.getValue() + " ");
		System.out.print("} ");
	}

	TrieNode getParent() 
	{
		return parent;
	}

	void setParent(TrieNode parent) 
	{
		this.parent = parent;
	}

	ArrayList<TrieNode> getMembers() 
	{
		return members;
	}

	void setMembers(ArrayList<TrieNode> members) 
	{
		this.members = members;
	}

	char getValue() 
	{
		return value;
	}

	void setValue(char value) 
	{
		this.value = value;
	}

	boolean isEnd() 
	{
		return isEnd;
	}

	void setEnd(boolean isEnd)
	{
		this.isEnd = isEnd;
	}

	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + value;
		return result;
	}

	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TrieNode other = (TrieNode) obj;
		if (value != other.value)
			return false;
		return true;
	}

	public int compareTo(TrieNode o) 
	{

		if (this.value == o.value)
			return 0;

		return this.value > o.value ? 1 : -1;
	}
}