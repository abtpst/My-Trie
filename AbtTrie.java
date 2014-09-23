/*
 * This class represents the Trie. It is responsible for adding words to and searching for words in the Trie.
 * It also has the function for displaying the Trie in a level by level fashion
 *  */
package itried;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class AbtTrie 
{
	// Root TrieNode. This represents the sentinel Node
	TrieNode root = new TrieNode();
	// This is the prefix we would search for if needed
	String prefix = "";
	// This is the wordList for the above prefix
	ArrayList<String> wordList = new ArrayList<String>();

	AbtTrie() {}

	// Function for adding new words
	void grow(String str) 
	{
		root.addMembers(str);
	}

	// Function for displaying the Trie level by level
	void showTrie() 
	{
		// We maintain two Queues for storing and retrieving member TrieNodes
		Queue<TrieNode> A = new LinkedList<TrieNode>();
		Queue<TrieNode> B = new LinkedList<TrieNode>();

		// First, add the root to the Queue A
		A.add(root);

		while (true) 
		{
			// While Queue A is not empty, 
			while (!A.isEmpty()) 
			{
				// get its first element and add show all of its children of its children
				TrieNode aNode = A.poll();

				if (!aNode.getMembers().isEmpty())
					System.out.print(aNode.getValue() + "->");
				aNode.showMembers();

				// Now add all of its children to Queue B
				B.addAll(aNode.getMembers());
			}

			// New line represents the next level
			System.out.println();

			// Now repeat the same process but now the roles of the two Queues are reversed
			while (!B.isEmpty()) 
			{
				TrieNode bNode = B.poll();

				if (!bNode.getMembers().isEmpty())
					System.out.print(bNode.getValue() + "->");
				bNode.showMembers();

				A.addAll(bNode.getMembers());
			}

			// New line represents the next level
			System.out.println();

			// If both Queues are emoty then we are done
			if (A.isEmpty() && B.isEmpty())
				break;
		}
	}

	// This function searches for and optionally adds a word to the Trie.
	void findNadd(String string, boolean add) 
	{
		root.searchNadd(string, add);
	}

	// This function searches for a prefix in the Trie
	void prefixSearch(String str) 
	{
		prefix = str;
		clearWords();
		root.preFind(str, prefix, wordList);

		for (String s : wordList)
			System.out.println(s);
	}

	void clearWords() 
	{
		wordList.clear();
	}
}