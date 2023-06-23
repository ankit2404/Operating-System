package Real;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.CountDownLatch;

public class first {
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
            lock.lock();
            try {
                return new ArrayList<>(child);
            } finally {
                lock.unlock();
            }
        }

        public void addChild(Node c) {
            lock.lock();
            try {
                if (!child.contains(c)) {
                    child.add(c);
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
                return isLocked;
            } finally {
                lock.unlock();
            }
        }

        public void setLock() {
            lock.lock();
            try {
                isLocked = true;
            } finally {
                lock.unlock();
            }
        }

        public void removeLock() {
            lock.lock();
            try {
                isLocked = false;
            } finally {
                lock.unlock();
            }
        }

        public int getAnc_count() {
            lock.lock();
            try {
                return anc_locked;
            } finally {
                lock.unlock();
            }
        }

        public void setAnc_count(int val) {
            lock.lock();
            try {
                anc_locked += val;
            } finally {
                lock.unlock();
            }
        }

        public int getId() {
            lock.lock();
            try {
                return id;
            } finally {
                lock.unlock();
            }
        }

        public void setId(int val) {
            lock.lock();
            try {
                id = val;
            } finally {
                lock.unlock();
            }
        }

        public int getDesc_count() {
            lock.lock();
            try {
                return des_locked;
            } finally {
                lock.unlock();
            }
        }

        public void setDesc_count(int val) {
            lock.lock();
            try {
                des_locked += val;
            } finally {
                lock.unlock();
            }
        }
    }

    static void change(Node node, int val) {
        if (node == null) {
            return;
        }
        node.setAnc_count(val);
        for (Node i : node.getChildren()) {
            change(i, val);
        }
    }

    static boolean lock(Node node, int id) {
        if (!node.isLocked() && node.getAnc_count() == 0 && node.getDesc_count() == 0) {
            if (node.lock.tryLock()) {
                try {
                    Node parent = node.getParent();
                    while (parent != null) {
                        parent.setDesc_count(1);
                        parent = parent.getParent();
                    }

                    change(node, 1);
                    node.setLock();
                    node.setId(id);

                    return true;
                } finally {
                    node.lock.unlock();
                }
            }
        }

        return false;
    }

    static boolean unlock(Node node, int id) {
        if (node.lock.tryLock()) {
            try {
                if (node.isLocked() && node.getId() == id) {
                    Node parent = node.getParent();
                    while (parent != null) {
                        parent.setDesc_count(-1);
                        parent = parent.getParent();
                    }

                    change(node, -1);
                    node.removeLock();
                    node.setId(0);

                    return true;
                }
            } finally {
                node.lock.unlock();
            }
        }

        return false;
    }

    static boolean getAllChilds(Node node, ArrayList<Node> a, int id) {
        if (node == null) {
            return true;
        }

        if (node.lock.tryLock()) {
            try {
                if (node.isLocked()) {
                    if (id != node.getId()) {
                        return false;
                    } else {
                        a.add(node);
                    }
                }

                if (node.getDesc_count() == 0) {
                    return true;
                }
            } finally {
                node.lock.unlock();
            }
        }

        for (Node i : node.getChildren()) {
            boolean ans = getAllChilds(i, a, id);
            if (!ans)
                return false;
        }

        return true;
    }

    static boolean upgrade(Node node, int id) {
        if (!node.isLocked() && node.getAnc_count() == 0 && node.getDesc_count() > 0) {
            if (node.lock.tryLock()) {
                try {
                    ArrayList<Node> a = new ArrayList<>();
                    boolean can = getAllChilds(node, a, id);
                    if (!can)
                        return false;

                    change(node, 1);

                    for (Node i : a) {
                        if (!unlock(i, id)) {
                            return false;
                        }
                    }

                    node.setLock();
                    node.setId(id);
                    return true;
                } finally {
                    node.lock.unlock();
                }
            }
        }

        return false;
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
        CountDownLatch latch = new CountDownLatch(q);
        List<String> results = Collections.synchronizedList(new ArrayList<>());
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

                results.add(ans + " " + threads[finalI].getName());

                // Count down the latch to signal the thread's completion
                latch.countDown();
            });

            // Start the thread
            threads[i].start();
        }

        try {
            // Wait for all threads to finish
            latch.await();
        } catch (InterruptedException e) {
//            e.printStackTrace();
            System.out.println(e);
        }
        for (String result : results) {
            System.out.println(result);
        }
    }
}
