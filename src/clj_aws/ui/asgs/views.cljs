(ns clj-aws.ui.asgs.views
  (:require [re-frame.core :refer [subscribe dispatch]]))


(defn asgs-list
  []
  (let [asgs (subscribe [:asgs])
        loading-asgs? (subscribe [:loading-asgs?])]
    (fn []
      (cond 
        (> (count @asgs) 0) [:div {:class "bootcards-list"}
                             [:div {:class "panel panel-default"}
                              [:div {:class "list-group"}
                               (for [asg @asgs]
                                 (let [asg-name (first (keys asg))
                                       instances (first (vals asg))]
                                   ^{:key asg-name} [:a {:class "list-group-item"
                                                         :href "#"
                                                         :on-click #(dispatch [:fetch-ec2-instances (name asg-name)])}
                                                     [:div {:class "row"}
                                                      [:div {:class "col-sm-6"}
                                                       [:h4 {:class "list-group-item-heading"} (name asg-name)]
                                                       [:p {:class "list-gorup-item-text"} (str (count instances) " Instances")]]]]))]]]
        (true? @loading-asgs?) [:h1 "Loading ASGs....."]
        :else               [:h1 "NO ASGS AVAILABLE"]))))

(defn vpcs-list
  []
  (let [vpcs          (subscribe [:vpcs])
        asgs          (subscribe [:asgs])
        loading-vpcs? (subscribe [:loading-vpcs?])
        selected-asg  (subscribe [:selected-asg])]
    (fn []
      (cond 
        (empty? @vpcs)         [:div {:class "row"}
                                [:h1 (str (count @asgs) " ASGS found.")]]
        (true? @loading-vpcs?) [:div.row [:h1 "Loading VPCs"]]
        :else                  [:div {:class "row"}
                                [:div.bootcards-list
                                 [:div {:class "panel panel-default"}
                                  [:div.list-group
                                   [:div.list-group-item
                                    [:h4.list-group-item-heading @selected-asg]]
                                   (for [vpc @vpcs]
                                     (let [instance-id (:instance-id vpc)
                                           private-dns (:private-dns-name vpc)
                                           private-ip  (:private-ip-address vpc)
                                           public-dns  (:public-dns-name vpc)
                                           public-ip   (:public-ip-address vpc)
                                           status      (get-in vpc [:state :name])]
                                       ^{:key vpc} [:a {:class "list-group-item"
                                                        :href "#"}
                                                    [:h4 {:class "list-group-item-heading"} instance-id]
                                                    [:p {:class "list-group-item-text"}
                                                     [:div
                                                      [:label "Status: " status]]
                                                     [:div
                                                      [:span "Private DNS " private-dns]]
                                                     [:div 
                                                      [:span "Private IP " private-ip]]
                                                     [:div
                                                      [:span "Public DNS " public-dns]]
                                                     [:div
                                                      [:span "Public IP " public-ip]]]]))]]]]))))


(defn main-panel
  []
  (fn []
    [:div {:class "row"}
     [:div {:class "col-md-4"}
      [asgs-list]]
     [:div {:class "col-md-6"}
      [vpcs-list]]]))


