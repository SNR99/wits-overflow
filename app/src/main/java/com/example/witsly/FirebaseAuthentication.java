package com.example.witsly;

import com.example.witsly.Interfaces.FirebaseAuthHandler;
import com.example.witsly.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

class FirebaseAuthentication {

  private final FirebaseAuth mAuth;

  FirebaseAuthentication() {
    mAuth = FirebaseAuth.getInstance();
  }

  void logout(final FirebaseAuthHandler r) {
    FirebaseAuth.getInstance().signOut();
    r.processAuth(true, "");
  }

  void login(String email, String password, FirebaseAuthHandler f) {
    mAuth
        .signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(
            task -> {
              if (task.isSuccessful()) f.processAuth(true, "");
            })
        .addOnFailureListener(
            e -> {
              f.processAuth(false, e.getMessage());
            });
  }

  void resetPassword(String email, FirebaseAuthHandler f) {
    mAuth
        .sendPasswordResetEmail(email)
        .addOnSuccessListener(
            aVoid -> {
              f.processAuth(true, "");
            })
        .addOnFailureListener(
            e -> {
              f.processAuth(false, e.getMessage());
            });
  }

  void register(String email, String name, String surname, String password, FirebaseAuthHandler f) {
    final FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    mAuth
        .createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(
            task -> {
              if (task.isSuccessful()) {
                final User user = new User(name, surname, email);

                mFirebaseDatabase
                    .getReference("Users")
                    .child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                    .setValue(user)
                    .addOnCompleteListener(
                        task1 -> {
                          f.processAuth(task1.isSuccessful(), "");
                        })
                    .addOnFailureListener(e -> f.processAuth(false, e.getMessage()));

                FirebaseAuth.getInstance().signOut();
              }
            })
        .addOnFailureListener(e -> f.processAuth(false, e.getMessage()));
  }
}
