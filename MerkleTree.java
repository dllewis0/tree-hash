import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Creates a binary hash tree.
 * 
 * @author Danielle Lewis 
 * @version 4-18-2017
 */
public class MerkleTree
{
    String hashKey;
    String timeStamp;
    String simpleHash;
    int transactionID;
    MerkleTree parent, leftChild, rightChild;
    int transactionAmount; //randomized
    
    /**
     * Constructor for objects of class MerkleTree
     * 
     * p - parent node
     * thisKey - new key to use to help create hash
     */
    public MerkleTree(MerkleTree p, String thisKey)
    {
        Random theRandomizer = new Random();
        transactionAmount = theRandomizer.nextInt(100);
        parent = p;
        createHashKey(thisKey);
        simpleHash = hashFunction(thisKey);
        timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
    }
    
    /**
     * Makes transactionAmount accessible
     */
    public int getTransactionAmount()
    {
        return transactionAmount;
    }
    
    /**
     * Makes hashKey accessible
     */
    public String getHashKey()
    {
        return hashKey;
    }
    
    /**
     * Makes parent accessible
     */
    
    public MerkleTree getParent()
    {
        return parent;
    }
    
    /**
     * Creates a unique hash for this object:
     * if root, hash created with just thisKey.
     * if parent but no grandparent, hash created as hash(thisKey + hash(parentKey))
     * if parent and grandparent, hash created as hash(thisKey + hash(parentKey + grandparentKey))
     */
    private void createHashKey(String thisKey)
    {
        if(hasGrandparent())
        {
            hashKey = hashFunction(thisKey + parent.getHashKey() + parent.getParent().getHashKey());
        }
        else if(hasParent())
        {
            hashKey = hashFunction(thisKey + parent.getHashKey());
        }
        else
        {
            hashKey = hashFunction(thisKey);
        }
    }
    
    /**
     * returns true if parent is not null, false otherwise
     */
    public boolean hasParent()
    {
        if(parent != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * returns true if parent of parent is not null, false otherwise
     */
    public boolean hasGrandparent()
    {
        if(hasParent() && parent.hasParent())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * Used for validating key only.
     */
    public String getSimpleHash()
    {
        return simpleHash;
    }
    
    /**
     * Reused from assignment 9.
     * Switched to public so driver can access it.
     * Creates a hash using SHA-256.
     * Did have to look this one up a bit as well. http://stackoverflow.com/questions/5531455/how-to-hash-some-string-with-sha256-in-java
     * It seems to make sense.
     */
    public String hashFunction(String withWhat)
    {   
        try {
            MessageDigest theDigest = MessageDigest.getInstance("SHA-256");
            
            byte[] baseHash = theDigest.digest(withWhat.getBytes("UTF-8"));
            StringBuffer hashToHex = new StringBuffer();
            
            for(int i = 0; i < baseHash.length; i++)
            {
                String resultHex = Integer.toHexString(0xff & baseHash[i]);
                
                if(resultHex.length() == 1)
                {
                    hashToHex.append('0');
                    hashToHex.append(resultHex);
                }
            }
            
            return hashToHex.toString();
        } catch(NoSuchAlgorithmException|UnsupportedEncodingException e) {
            System.out.println(e);
        };

        return "";
    }
    
    /**
     * Makes timestamp accessible
     */
    public String getTimeStamp()
    {
        return timeStamp;
    }
    
    /**
     * Sets left child
     * returns tid + 1 in order to keep incrementing transactionIDs
     */
    public int setLeftChild(String newKey, int tid)
    {
        leftChild = new MerkleTree(this, newKey);
        leftChild.setTransactionID(tid);
        
        return tid + 1;
    }
    
    /**
     * Sets right child
     * returns tid + 1 in order to keep incrementing transactionIDs
     */
    public int setRightChild(String newKey, int tid)
    {
        rightChild = new MerkleTree(this, newKey);
        rightChild.setTransactionID(tid);
        
        return tid + 1;
    }
    
    /**
     * Next 2 functions make children accessible
     */
    
    public MerkleTree getLeftChild()
    {
        return leftChild;
    }
    
    public MerkleTree getRightChild()
    {
        return rightChild;
    }
    
    /**
     * Next 2 functions are getter and setter for transactionID
     */
    public int getTransactionID()
    {
        return transactionID;
    }
    
    /**
     * returns an incremented value that can then be used for the next setting of transaction ID
     */
    public int setTransactionID(int theID)
    {
        transactionID = theID;
        
        return transactionID + 1;
    }
}
