package net.minecraft.src;

class NetworkWriterThread extends Thread {
    // $FF: synthetic field
    final NetworkManager netManager;

    NetworkWriterThread(NetworkManager var1, String var2) {
        super(var2);
        this.netManager = var1;
    }

    public void run() {
        synchronized(NetworkManager.threadSyncObject) {
            ++NetworkManager.numWriteThreads;
        }

        while(true) {
            boolean var11 = false;

            try {
                var11 = true;
                if (!NetworkManager.isRunning(this.netManager)) {
                    var11 = false;
                    break;
                }

                NetworkManager.sendNetworkPacket(this.netManager);
            } finally {
                if (var11) {
                    synchronized(NetworkManager.threadSyncObject) {
                        --NetworkManager.numWriteThreads;
                    }
                }
            }
        }

        synchronized(NetworkManager.threadSyncObject) {
            --NetworkManager.numWriteThreads;
        }
    }
}
