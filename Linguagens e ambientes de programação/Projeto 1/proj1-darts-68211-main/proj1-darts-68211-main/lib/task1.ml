open Types

(*Leandro 68211*)


let singles = List.init 20 (fun i -> S (i + 1)) @ [S 25]
let doubles = List.init 20 (fun i -> D (i + 1)) @ [D 25]
let triples = List.init 20 (fun i -> T (i + 1))
let all_throws = singles @ doubles @ triples

let throw_score t =
  match t with
  | S n -> n
  | D n -> 2 * n
  | T n -> 3 * n
  

let rec compute_checkouts_aux left_score building_list max_throws =
  if left_score = 0 && 
     match List.rev building_list with
     | [] -> false
     | h::t -> 
         match h with
         | D _ -> true  
         | _ -> false
  then
    [building_list] 
  else if max_throws = 0  then
    [] 
  else
    List.fold_left (fun acc t ->
      if throw_score t <= left_score then 
        acc @ compute_checkouts_aux (left_score - throw_score t) (t :: building_list) (max_throws - 1)
      else acc) [] all_throws

  let compute_checkouts (score: int) : checkouts = compute_checkouts_aux score [] 3
