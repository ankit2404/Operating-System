import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TOS {
    static class Node {
        private boolean isLocked;
        private int id;
        private Node parent;
        private int anc_locked;
        private int des_locked;
        private final Lock lock;
        private ArrayList<Node> child;

        public Node() {
            this.child = new ArrayList<>();
            this.parent = null;
            this.isLocked = false;
            this.des_locked = 0;
            this.lock = new ReentrantLock();
        }

        public List<Node> getChildren() {
            synchronized (child) {
                return new ArrayList<>(child);
            }
        }

        public void addChild(Node c) {
            synchronized (child) {
                if (!child.contains(c)) {
                    child.add(c);
                }
            }
        }

        public void setParent(Node parent) {
            synchronized (this) {
                this.parent = parent;
            }
        }

        public Node getParent() {
            synchronized (this) {
                return parent;
            }
        }

        public boolean isLocked() {
            synchronized (this) {
                return isLocked;
            }
        }

        public void setLock() {
            synchronized (this) {
                isLocked = true;
            }
        }

        public void removeLock() {
            synchronized (this) {
                isLocked = false;
            }
        }

        public int getAnc_count() {
            synchronized (this) {
                return anc_locked;
            }
        }

        public void setAnc_count(int val) {
            synchronized (this) {
                anc_locked += val;
            }
        }

        public int getId() {
            synchronized (this) {
                return id;
            }
        }

        public void setId(int val) {
            synchronized (this) {
                id = val;
            }
        }

        public int getDesc_count() {
            synchronized (this) {
                return des_locked;
            }
        }

        public void setDesc_count(int val) {
            synchronized (this) {
                des_locked += val;
            }
        }
    }

    static void change(Node node, int val) {
        if (node == null) {
            return;
        }
        synchronized (node) {
            node.setAnc_count(val);
        }
        synchronized (node.child) {
            for (Node i : node.child) {
                change(i, val);
            }
        }
    }

    static boolean lock(Node node, int id) {

            if (node.isLocked()) {
                return false;
            }

            if (node.getAnc_count() > 0 || node.getDesc_count() > 0) {
                return false;
            }

            Node parent = node.getParent();
            while (parent != null) {
                synchronized (parent) {
                    parent.setDesc_count(1);
                }
                parent = parent.getParent();
            }

            change(node, 1);
            node.setLock();
            node.setId(id);

            return true;
        }

    static boolean unlock(Node node, int id) {
        synchronized (node) {
            if (!node.isLocked() || node.getId() != id) {
                return false;
            }

            Node parent = node.getParent();
            while (parent != null) {
                synchronized (parent) {
                    parent.setDesc_count(-1);
                }
                parent = parent.getParent();
            }

            change(node, -1);
            node.removeLock();
            node.setId(0);

            return true;
        }
    }

    static boolean getAllChilds(Node node, ArrayList<Node> a, int id) {
        if (node == null) {
            return true;
        }

        synchronized (node) {
            if (node.isLocked()) {
                if (id != node.getId())
                    return false;
                else
                    a.add(node);
            }

            if (node.getDesc_count() == 0) {
                return true;
            }
        }

        synchronized (node.child) {
            for (Node i : node.getChildren()) {
                boolean ans = getAllChilds(i, a, id);
                if (!ans)
                    return false;
            }
        }

        return true;
    }

    static boolean upgrade(Node node, int id) {
        synchronized (node) {
            if (node.isLocked() || node.getAnc_count() > 0 || node.getDesc_count() == 0) {
                return false;
            }

            ArrayList<Node> a = new ArrayList<>();
            boolean can = getAllChilds(node, a, id);
            if (!can)
                return false;

            change(node, 1);

            for (Node i : a) {
                unlock(i, id);
            }

            node.setLock();
            node.setId(id);
            return true;
        }
    }

    public static void main(String[] args) {
        int n = 7;
        int k = 2;
        HashMap<String, Node> hash = new HashMap<>();
        String[] arr = new String[7];
        arr[0] = "World";
        arr[1] = "Asia";
        arr[2] = "Africa";
        arr[3] = "China";
        arr[4] = "India";
        arr[5] = "SouthAfrica";
        arr[6] = "Egypt";
        Node root = new Node();
        hash.put(arr[0], root);

        Queue<Node> que = new LinkedList<>();
        que.add(root);

        int index = 1;
        while (que.size() != 0 && index < n) {
            int size = que.size();
            while (size-- > 0) {
                Node rem = que.remove();
                for (int i = 1; i <= k && index < n; i++) {
                    Node newNode = new Node();
                    newNode.setParent(rem);
                    hash.put(arr[index], newNode);
                    rem.addChild(newNode);
                    que.add(newNode);
                    index += 1;
                }
            }
        }

        int q = 5;
        int[] a1 = new int[]{1, 1, 3, 2, 2};
        String[] a2 = new String[]{"China", "India", "Asia", "India", "Asia"};
        int[] a3 = new int[]{9, 9, 9, 9, 9};

        // Create an array of threads
        Thread[] threads = new Thread[q];

        for (int i = 0; i < q; i++) {
            int val = a1[i];
            String str = a2[i];
            int id = a3[i];

            final Node node = hash.get(str);

            // Create a new thread for each operation
            int finalI = i;
            threads[i] = new Thread(() -> {
                boolean ans = false;
                if (val == 1) {
                    ans = lock(node, id);
                } else if (val == 2) {
                    ans = unlock(node, id);
                } else {
                    ans = upgrade(node, id);
                }

                System.out.println(ans + " " + threads[finalI].getName());
            });

            // Start the thread
            threads[i].start();
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
