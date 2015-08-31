(ns clj-aws.ui.asgs.views
  (:require [re-frame.core :refer [subscribe dispatch]]))


(defn asgs-list
  []
  (let [asgs (subscribe [:asgs])]
    (fn []
      (if (> (count @asgs) 0)
        [:div {:class "bootcards-list"}
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
                                  [:p {:class "list-gorup-item-text"} (str (count instances) " Instances")]]]]))]]
        [:h1 "NO ASGS AVAILABLE"]))))

(defn vpcs-list
  []
  (let [vpcs (subscribe [:vpcs])
        asgs (subscribe [:asgs])]
    (fn []
      (if (empty? @vpcs)
        [:div {:class "row"}
         [:h1 (str (count @asgs) " ASGS found.")]]
        [:div {:class "row"}
         [:div.bootcards-list
          [:div {:class "panel panel-default"}
           [:div.list-group
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

