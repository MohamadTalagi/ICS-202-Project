# ICS-202-Project
KFUPM Clinic System 

Students Contributing to the project are:
-Nawaf AlSalem: 202444800
-Mohamad Talagi: 202341490

Task Split:

# ✅ Mohamad's Checklist (Core + Undo + Scheduling)**
Data Structures:
Implement hash table core (node, array, hashing)
📁 ds/HashTable.java
💾 Commit: Init hash table structure
 Implement put and get
📁 ds/HashTable.java
💾 Commit: Implement hash put/get
 Implement remove and size
📁 ds/HashTable.java
💾 Commit: Implement hash remove/size
 Implement collision handling + resizing
📁 ds/HashTable.java
💾 Commit: Add hash collisions and resizing
 Implement AVL node structure + height helpers
📁 ds/AVLTree.java
💾 Commit: Init AVL structure
 Implement AVL insert (put) with rotations
📁 ds/AVLTree.java
💾 Commit: Implement AVL insert + rotations
 Implement AVL search (get)
📁 ds/AVLTree.java
💾 Commit: Implement AVL search
 Implement AVL inOrder traversal
📁 ds/AVLTree.java
💾 Commit: Implement AVL traversal
 Implement AVL minEntry
📁 ds/AVLTree.java
💾 Commit: Implement AVL minEntry
 Implement AVL remove + rebalancing
📁 ds/AVLTree.java
💾 Commit: Implement AVL delete
 Implement stack (push/pop/peek/isEmpty)
📁 ds/LinkedStack.java
💾 Commit: Implement linked stack
Service Logic
 Implement ADD_PATIENT
📁 service/ClinicServiceImpl.java
💾 Commit: Add patient creation
 Implement FIND_PATIENT
📁 service/ClinicServiceImpl.java
💾 Commit: Add patient lookup
 Implement DELETE_PATIENT
📁 service/ClinicServiceImpl.java
💾 Commit: Add patient deletion
 Implement appointment ID generation
📁 service/ClinicServiceImpl.java
💾 Commit: Add appointment ID generation
 Implement ADD_APPT (store in AVL + HashTable)
📁 service/ClinicServiceImpl.java
🔗 depends on: HashTable + AVL ready
💾 Commit: Add appointment creation
 Implement FIND_APPT
📁 service/ClinicServiceImpl.java
💾 Commit: Add appointment lookup
 Implement CANCEL_APPT
📁 service/ClinicServiceImpl.java
💾 Commit: Add appointment cancel
 Implement VIEW_DAY (using AVL traversal)
📁 service/ClinicServiceImpl.java
💾 Commit: Add day schedule view
 Implement VIEW_RANGE
📁 service/ClinicServiceImpl.java
💾 Commit: Add range schedule view
Undo System (GLOBAL OWNER)
 Design undo action handling using Action
📁 service/ClinicServiceImpl.java
💾 Commit: Setup undo framework
 Implement undo for ADD_APPT
📁 service/ClinicServiceImpl.java
💾 Commit: Undo add appointment
 Implement undo for CANCEL_APPT
📁 service/ClinicServiceImpl.java
💾 Commit: Undo cancel appointment
 Implement undo for ADD_WALKIN
📁 service/ClinicServiceImpl.java
🔗 depends on: queue from Member 2
💾 Commit: Undo walk-in
 Implement undo for ADD_URGENT
📁 service/ClinicServiceImpl.java
🔗 depends on: heap from Member 2
💾 Commit: Undo urgent
 Implement undo for SERVE_NEXT
📁 service/ClinicServiceImpl.java
🔗 depends on: serving logic from Member 2
💾 Commit: Undo serve

# ✅ Nawaf's Checklist (Serving + Structures + Search)
Data Structures
 Implement queue (enqueue/dequeue/isEmpty)
📁 ds/LinkedQueue.java
💾 Commit: Implement queue core
 Implement queue toList
📁 ds/LinkedQueue.java
💾 Commit: Add queue view
 Implement singly linked list
📁 ds/SinglyLinkedList.java
💾 Commit: Implement linked list
 Implement heap internal storage
📁 ds/MaxHeap.java
💾 Commit: Init heap structure
 Implement push and peek
📁 ds/MaxHeap.java
💾 Commit: Implement heap push/peek
 Implement pop
📁 ds/MaxHeap.java
💾 Commit: Implement heap pop
 Implement heap snapshot
📁 ds/MaxHeap.java
💾 Commit: Add heap snapshot
String Matching
 Fix matcher package to matching/
📁 matching/*
💾 Commit: Fix matcher package
 Implement naive matching
📁 matching/NaiveMatcher.java
💾 Commit: Implement naive matcher
 Implement KMP prefix/LPS
📁 matching/KMPMatcher.java
💾 Commit: Add KMP prefix
 Implement KMP search
📁 matching/KMPMatcher.java
💾 Commit: Implement KMP search
Service Logic
 Implement ADD_WALKIN
📁 service/ClinicServiceImpl.java
🔗 depends on: patient lookup (Member 1)
💾 Commit: Add walk-in
 Implement VIEW_WALKINS
📁 service/ClinicServiceImpl.java
💾 Commit: View walk-ins
 Implement ADD_URGENT
📁 service/ClinicServiceImpl.java
🔗 depends on: patient lookup
💾 Commit: Add urgent patient
 Implement PEEK_URGENT
📁 service/ClinicServiceImpl.java
💾 Commit: Peek urgent
 Implement VIEW_URGENTS
📁 service/ClinicServiceImpl.java
💾 Commit: View urgent list
Serving System
 Implement serve from urgent
📁 service/ClinicServiceImpl.java
💾 Commit: Serve urgent
 Implement serve from walk-in
📁 service/ClinicServiceImpl.java
💾 Commit: Serve walk-in
 Implement serve from appointments (using AVL min)
📁 service/ClinicServiceImpl.java
🔗 depends on: AVL complete (Member 1)
💾 Commit: Serve appointment
 Combine into full SERVE_NEXT logic
📁 service/ClinicServiceImpl.java
💾 Commit: Finalize serve logic
Log + Search
 Implement visit log insertion
📁 service/ClinicServiceImpl.java, ds/SinglyLinkedList.java
💾 Commit: Add visit log
 Implement PRINT_LOG
📁 service/ClinicServiceImpl.java
💾 Commit: Print log
 Implement SEARCH_LOG_NAIVE
📁 service/ClinicServiceImpl.java, matching/NaiveMatcher.java
💾 Commit: Search log naive
 Implement SEARCH_LOG_KMP
📁 service/ClinicServiceImpl.java, matching/KMPMatcher.java
💾 Commit: Search log KMP
