(ns dojotext.core)

(require '[clojure.string :as str])

;;(def countries (pprint (split (slurp "data/data.txt") #"\d+ of 42")))

(def countries (str/split (slurp "data/data.txt") #"\d+ of 42"))

;;(defn country-name [s] (re-find #".*\s([A-Za-z]*\.)" s))

(defn country-name [s] (second (re-find #"[^\.]*\s([A-Z][a-z]+)[\.,]" s)))

(defn points [[_ score country]]
    [(Integer/parseInt (if score score "0")) country])
    
;;(defn country-scores [s] [(country-name s) (map points (re-seq #"(\d+)(:?\spoints)? to ([A-Z][a-z]+)" s))])

(defn country-scores [s] [(country-name s) (map points (re-seq #"(\d+)(?:\spoints)?(?:\sgo)? to [^A-Z]*([A-Z][a-z]+)" s))])

(defn voted-for? [a b vote-map] (when (some #{b} (map second (vote-map a))) 
    a))
 
(defn recip-votes [a vote-map]
    (remove nil? (map #(voted-for? % a vote-map) (map second (vote-map a)))))

(def countries-and-scores (into {} (map country-scores countries)))
    
(defn all-recip [vote-map] (filter (fn [[_ x]] (seq x)) (map (fn [c] [c (recip-votes c vote-map)]) (keys vote-map))))

(defn -main [] (all-recip countries-and-scores))