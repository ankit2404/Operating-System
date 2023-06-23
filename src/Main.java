import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Node {
    private String name;
    private List<Node> children;
    private Node parent;
    private boolean locked;
    public int lockedChildCount;
    public Lock lock;

    public Node(String name, Node parent) {
        this.name = name;
        this.children = new ArrayList<>();
        this.parent = parent;
        this.locked = false;
        this.lockedChildCount = 0;
        this.lock = new ReentrantLock();
        if (parent != null) {
            parent.addChild(this);
        }
    }

    public List<Node> getChildren() {
        lock.lock();
        try {
            return new ArrayList<>(children);
        } finally {
            lock.unlock();
        }
    }

    public void addChild(Node child) {
        lock.lock();
        try {
            if (!children.contains(child)) {
                children.add(child);
            }
        } finally {
            lock.unlock();
        }
    }

    public void setParent(Node parent) {
        lock.lock();
        try {
            this.parent = parent;
        } finally {
            lock.unlock();
        }
    }

    public Node getParent() {
        lock.lock();
        try {
            return parent;
        } finally {
            lock.unlock();
        }
    }

    public boolean isLocked() {
        lock.lock();
        try {
            return locked;
        } finally {
            lock.unlock();
        }
    }

    public void setLock() {
        lock.lock();
        try {
            locked = true;
        } finally {
            lock.unlock();
        }
    }

    public void removeLock() {
        lock.lock();
        try {
            locked = false;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return name;
    }
}

public class Main {
    public static boolean lock(Node node) {
        if (!tryLock(node)) {
            return false;
        }

//        if(node.lockedChildCount > 0){
//            return false;
//        }
        try {

            Node lockedParent = null;
            Node parentNode = node.getParent();
            while (parentNode != null) {
                if (tryLock(parentNode) == false) {
                    lockedParent = parentNode;
                    break;
                }
                parentNode.lockedChildCount++;
                parentNode = parentNode.getParent();
            }

            if (lockedParent != null) {
                parentNode = node.getParent();
                while (parentNode != lockedParent) {
                    parentNode.lockedChildCount--;
                    parentNode = parentNode.getParent();
                }
                node.removeLock();
                return false;
            }

            return true;
        } finally {
            unlock(node);
        }
    }

    private static boolean tryLock(Node node) {
        return node.lock.tryLock();
    }

    private static void unlock(Node node) {
        node.lock.lock();
        try {
            node.removeLock();
        } finally {
            node.lock.unlock();
        }
    }

    public static boolean upgrade(Node node) {
        node.lock.lock();
        try {
            if (!node.isLocked() || node.lockedChildCount > 0) {
                return false;
            }

            Node parentNode = node.getParent();
            while (parentNode != null) {
                parentNode.lock.lock();
                parentNode.setLock();
                parentNode.lock.unlock();
                parentNode = parentNode.getParent();
            }

            node.lockedChildCount--;
            return true;
        } finally {
            node.lock.unlock();
        }
    }

    public static void main(String[] args) {
        Node nodeA = new Node("Node A", null);
        Node nodeB = new Node("Node B", nodeA);
        Node nodeC = new Node("Node C", nodeA);
        Node nodeD = new Node("Node D", nodeB);
        Node nodeE = new Node("Node E", nodeB);
        Node nodeF = new Node("Node F", nodeC);

        // Lock nodeE
        boolean result = lock(nodeE);
        System.out.println("Locking nodeE: " + result);  // Should print true

        // Upgrade nodeE (should fail because it has locked child nodes)
        result = upgrade(nodeE);
        System.out.println("Upgrading nodeE: " + result);  // Should print false

        // Unlock nodeE
        unlock(nodeE);

        // Upgrade nodeE (should succeed now)
        result = upgrade(nodeE);
        System.out.println("Upgrading nodeE: " + result);  // Should print true
    }
}
