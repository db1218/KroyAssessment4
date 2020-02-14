package com.mozarellabytes.kroy.Utilities;

import com.badlogic.gdx.math.Vector2;

public class CircularLinkedList {

    private Node head = null;
    private Node tail = null;

    public void addNode(Vector2 data){
        Node node= new Node(data);
        if(head==null){
            head=node;
        }
        else{
            tail.nextNode=node;
        }
        tail=node;
        tail.nextNode=head;
    }

    public boolean containsNode (Vector2 searchValue){
        Node current = head;
        if(head == null){
            return false;
        }
        else{
            do{
                if(current.data==searchValue){
                    return true;
                }
                current = current.nextNode;
            } while (current != head);
            return false;
        }
    }

    public void deleteNode(Vector2 deleteValue){
        Node current = head;

        if(head!=null){
            if(current.data == deleteValue){
                head = head.nextNode;
                tail.nextNode = head;
            }
            else{
                do{
                    Node next = current.nextNode;
                    if(next.data == deleteValue){
                        current.nextNode = next.nextNode;
                        break;
                    }
                    current = current.nextNode;
                } while (current != head);
            }
        }
    }

    public void traverseList(){
        Node current=head;

        if(head!=null){
            do{
                System.out.println(current.data);
                current=current.nextNode;
            }while (current!=head);
        }
    }

    public Node getHead(){
        return head;
    }

    public Node getTail(){
        return tail;
    }

    public Node getNext(Node current){
        return current.nextNode;
    }

    public Vector2 getData(Node node){
        return node.data;
    }
}

