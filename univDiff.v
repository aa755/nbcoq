(* from http://comments.gmane.org/gmane.science.mathematics.logic.coq.club/5627 *)

Definition T1 := Type.
Definition T2 := Type.

Axiom X: T1 = T2.

Definition Y := (T1 -> T1): T2.

Lemma shouldnt_it_lead_to_universe_inconsistancy: True.
 destruct X.
 exact I.
Qed.

Print shouldnt_it_lead_to_universe_inconsistancy.
(* Shouldn't axiom X enforce that type universe T1 and type universe
T2 are on the same universe? *)

Lemma should_lead_to_universe_inconsistancy2: True.
 generalize (refl_equal Y); unfold Y.
 (* error: term is ill-typed; isn't it rather universe inconsistancy? *)
Abort.

(* Set Printing Universes. (* enable this or use your IDE menu*) *)

Lemma should_lead_to_universe_inconsistancy3: True.
 generalize (refl_equal Y); unfold Y.
 pattern T1.
 set (u := T1).
 case X.
 intros [].
 exact I.
 Print Universes. (* The output of this went to the file graphNoSelf.txt *)
 
Qed. (* the constraint mentioned in the error message is the last line of graphNoSelf.txt *)

