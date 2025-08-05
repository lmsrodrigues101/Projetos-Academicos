open Utils

type place = {
  name_p : string;
}

type transition = {
  name_t : string;
}

type arc = 
  | Outgoing_place_arc of place * transition * int   (* place -> transition *)
  | Incoming_place_arc of transition * place * int   (* transition -> place *)

type petri_net = {
  places : (place * int * int) list;
  transitions : transition list;
  arcs : arc list;
}

(*petri_net vazia*)
let empty : petri_net= {
  places = [];
  transitions = [];
  arcs = [];
}

let places net = List.map(fun (p,_,_) -> p) net.places

let transitions net = net.transitions

let arcs net = net.arcs

let compare_places p1 p2  = 
  if p1.name_p = p2.name_p then
    0
  else 1
  

let compare_transitions t1 t2 =
  if t1.name_t = t2.name_t then
    0
  else 1


let compare_arcs (a1:arc) (a2:arc) = 
  match (a1, a2) with
  |(Incoming_place_arc(t1,p1,w1), Incoming_place_arc(t2,p2,w2)) 
  |(Outgoing_place_arc(p1,t1,w1), Outgoing_place_arc(p2,t2,w2)) -> (compare_transitions t1 t2) + (compare_places p1 p2) + (w1-w2)
  | _ , _ -> 1

(*cria um place com um nome dado*)
let mk_place name : place = {
  name_p = name;
}
(*recebe um place,capacity,tokens,preti_net. Inicializa a place com a c e t e se a place ja existir na substitui*)
let add_place (p : place) c tokens (net:petri_net) = 
  let new_place : place = {name_p = p.name_p;} in 
  let rec process_places places =
      match places with 
      | [] -> [(new_place,c,tokens)]
      | (p1,c1,t1):: tl -> 
        if compare_places new_place p1 = 0 then 
          (new_place,c,tokens) :: tl
        else 
          (p1,c1,t1) :: process_places tl
    in 
    { net with places = process_places (net.places) }


(*Cria uma transition com o nome dado*)
let mk_transition name = {
    name_t = name;
}
 
(*Adiciona uma transition à petri net, caso essa transition já exista é substituida*)
let add_transition (t:transition) net  = 
  let rec process_transition transitions =
      match transitions with
      | [] -> t :: []
      | h :: tl -> 
        if compare_transitions t h = 0 then 
          t :: tl
        else 
          h :: process_transition tl
    in 
    { net with transitions = process_transition (transitions net) }


(*Cria um arc do place p para a transition t com o weight w*)
let mk_outgoing_arc p t w = Outgoing_place_arc(p,t,w)

(*Cria um arc da transition t para o place p com o weight w*)
let mk_incoming_arc t p w = Incoming_place_arc(t,p,w)

(*Verifica se um determinado place com o nome dado pertence à petri net*)
let verify_p name net = List.exists(fun (h,_,_) -> h.name_p = name ) (net.places)

(*Verifica se um determinado place com o nome dado pertence à petri net*)
let verify_t name net = List.exists(fun h -> h.name_t = name ) (transitions net)
  
(*Adiciona um arc à petri net, retorna None caso o arc não esteja bem definido na petri net*)
let add_arc (a:arc) net =
 match a with 
  | Outgoing_place_arc(p,t,_) | Incoming_place_arc(t,p,_) -> 
    if verify_p p.name_p net && verify_t t.name_t net then Some {net with arcs = a :: arcs net}
    else None

let place_info (p:place) net = 
  let rec aux lst = 
  match lst with
  | [] -> None
  | (p1,c1,t1) :: tl-> 
    if p1.name_p = p.name_p then
     Some (c1,t1)
    else aux tl
  in aux net.places


(*Retorn o numero de tokens do place p na petri net (net) dada, caso o place não exista na petri net retorna None*)
let marking_of_place p net =
  match place_info p net with
  | None -> None
  | Some (_,tokens) -> Some tokens
  
let capacity_of_place net (p:place) =
  match place_info p net with
  | None -> max_int
  | Some (capacity,_) -> capacity


let preceding_places (t:transition) net  = 
  let filtered_arcs = List.filter(fun h ->
     match h with
     | Outgoing_place_arc (_,tr,_) -> tr.name_t = t.name_t
     | _ -> false
   ) (arcs net) in
    List.map (fun arc -> 
      match arc with
      | Outgoing_place_arc(p,_,w)-> (p,w)
      | _ -> failwith "Expect arc -> Outgoing_place_arc"
    ) filtered_arcs


let succeding_places (t:transition) net = 
   let filtered_arcs = List.filter (fun h ->
     match h with
     | Incoming_place_arc (tr,_,_) -> tr.name_t = t.name_t
     | _ -> false
   ) (arcs net) in
    List.map (fun arc -> 
      match arc with
      | Incoming_place_arc(_,p,w)-> (p,w)
      | _ -> failwith "Expect arc -> Incoming_place_arc"
    ) filtered_arcs

let has_tokens (p,w) net =  
  match marking_of_place p net with
  | Some tokens -> tokens >= w
  | None -> false

let can_get_tokens (p,w) net =
  match marking_of_place p net with (*tokens + w <= capacity*)
  |Some tokens -> tokens + w <= capacity_of_place net p
  |None -> false

(*para todos os arcos de outgoing o place p desse arco tem que ter pelo menos (>=) w tokens, para todos os arcos incoming o place p tem de ter capacidade
para receber os w tokens do place do incoming*)

let is_enabled (t: transition) (net: petri_net) : bool =
  let input = preceding_places t net in
  let output = succeding_places t net in
  (* Se não houver arcos de saída, a transição não pode estar ativada *)
  if input = [] || output = [] then false
  else
    List.for_all (fun h -> has_tokens h net) input &&
    List.for_all (fun h -> can_get_tokens h net) output

let is_stuck net = 
  List.for_all (fun h -> not (is_enabled h net) ) (transitions net)


let remove_tokens (p,w) net =
 let change_place = List.map (fun (place,capacity,tokens) ->
    if compare_places place p = 0 then
      (place,capacity,tokens - w)
    else (place,capacity,tokens) ) net.places
  in 
  {net with places = change_place }

let add_tokens (p,w) net =
 let change_place = List.map (fun (place,capacity,tokens) ->
    if compare_places place p = 0 then
      (place,capacity,tokens + w)
    else (place,capacity,tokens) ) net.places
  in 
  {net with places = change_place }
  
  
let fire t net = 
  if not(verify_t t.name_t net) || not (is_enabled t net) then None
  else 
    let net_after_removes = List.fold_left (fun acc place_w -> remove_tokens place_w acc) net (preceding_places t net) in
    let net_after_addition = List.fold_left (fun acc place_w -> add_tokens place_w acc) net_after_removes (succeding_places t net) in
    Some net_after_addition

let fire_all (tl:transition list) (net: petri_net) = 
  let rec aux tl actual_net =
    match tl with
    | [] -> Some actual_net
    | h :: t ->
        match fire h actual_net with
        | None -> None
        | Some new_net -> aux t new_net
      in
    aux tl net 
    

    
  