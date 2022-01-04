package spell;

public class Trie implements ITrie {
    int numWords;
    int numNodes;
    Node root;

    public Trie() {
        numWords = 0;
        numNodes = 1; // root
        root = new Node();
    }

    public void add(String word) {
        addRecursive(word.toLowerCase(), root);
    }

    void addRecursive(String word, Node node) {
        if (word.length() == 0) { // base case
            node.incrementValue();
            if(node.getValue() == 1)
                numWords++; // first time word seen
        }
        else {
            int i = word.charAt(0) - 'a';
            if (node.letterArray[i] == null)  { // no node already there
                node.letterArray[i] = new Node(); // create node
                numNodes++;
            }
            addRecursive(word.substring(1), node.letterArray[i]);
        }
    }

    public INode find(String word) {
        return findRecursive(word.toLowerCase(), root);
    }

    INode findRecursive(String word, Node node) {
        if (word == null || word.length() == 0)
          return null;
        int i = word.charAt(0) - 'a';
        if (node.letterArray[i] == null)
          return null;
        else if (word.length() == 1 && node.letterArray[i].getValue() != 0)
            return node.letterArray[i];
        else if (word.length() == 1 && node.letterArray[i].getValue() == 0)
            return null;
        else
            return findRecursive(word.substring(1), node.letterArray[i]);
    }

    public int getWordCount() {
        return numWords;
    }

    public int getNodeCount() {
        return numNodes;
    }

    @Override
    public String toString() {
        StringBuilder word = new StringBuilder();
        StringBuilder output = new StringBuilder();
        toStringRecursive(root, word, output);
        return output.toString();
    }

    private void toStringRecursive(Node node, StringBuilder word, StringBuilder output) {
        if(node == null)
            return;
        if(node.getValue() > 0)
            output.append(word.toString() + "\n");
        for(int i = 0; i < 26; ++i) {
            word.append((char)(i + 'a'));
            toStringRecursive(node.letterArray[i], word, output);
            word.setLength(word.length() - 1);
        }
    }

    @Override
    public int hashCode() {
        return numNodes * numWords;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
			     return true;
		    if (obj == null)
			     return false;
		    if (getClass() != obj.getClass())
			     return false;

		    Trie other = (Trie) obj;
        return equalsRecursive(this.root, other.root);
    }

    boolean equalsRecursive(Node nodeA, Node nodeB) {
        for (int i = 0; i < 26; i++) {
            if (nodeA.letterArray[i] == null && nodeB.letterArray[i] == null)
                continue;
            else if (nodeA.letterArray[i] == null || nodeB.letterArray[i] == null)
                return false;
            else if (nodeA.letterArray[i].getValue() != nodeB.letterArray[i].getValue())
                return false;
            if (!equalsRecursive(nodeA.letterArray[i], nodeB.letterArray[i]))
                return false;
        }
        return true;
    }

    public class Node implements INode {
        int nodeValue;
        public Node[] letterArray;
        public Node() {
            nodeValue = 0;
            letterArray = new Node[26];
            for (int i = 0; i < 26; i++)
                letterArray[i] = null;
        }
        public int getValue() {
            return nodeValue;
        }
        public void incrementValue() {
            nodeValue++;
        }
    }
}
