//
//  MapGameViewController.swift
//  Tempus E Spatium
//
//  Created by Victor Poon on 15/10/2019.
//  Copyright © 2019 SoftFeta. All rights reserved.
//

import UIKit
import GoogleMaps
import SwiftIcons

class MapGameViewController: UIViewController, GMSMapViewDelegate {
    
    @IBOutlet weak var mInstructions: UILabel!
    
    @IBOutlet weak var mMap: GMSMapView!
    
    @IBOutlet weak var mResetBtn: UIButton!
    
    @IBOutlet weak var mSubmitBtn: UIButton!
    
    
    @IBAction func mResetBtnClicked(_ sender: Any) {
        mMap.clear()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
                mResetBtn.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeSolid(.undo), iconColor: .white, postfixText: NSLocalizedString("reset", comment: ""), postfixTextColor: .white, backgroundColor: UIColor(named: "danger")!, forState: .normal, textSize: nil, iconSize: nil)
        
        mSubmitBtn.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeSolid(.paperPlane), iconColor: .white, postfixText: NSLocalizedString("submit", comment: ""), postfixTextColor: .white, backgroundColor: UIColor(named: "success")!, forState: .normal, textSize: nil, iconSize: nil)
        
        mMap.delegate = self
        mMap.settings.tiltGestures = false
        mMap.settings.rotateGestures = false
        mMap.camera = GMSCameraPosition(latitude: CLLocationDegrees(exactly: 0)!, longitude: CLLocationDegrees(exactly: 0)!, zoom: 0)
        
        mMap.mapStyle = try! GMSMapStyle(jsonString: "[{\"elementType\":\"geometry.stroke\",\"stylers\":[{\"visibility\":\"off\"}]},{\"elementType\":\"labels\",\"stylers\":[{\"visibility\":\"off\"}]}]")
    }
    
    func mapView(_ mapView: GMSMapView, didTapAt coordinate: CLLocationCoordinate2D) {
        let marker = GMSMarker()
        mapView.clear()
        marker.isDraggable = false
        marker.position = coordinate
        marker.icon = UIImage(named: "rocket_pointer")!
        marker.appearAnimation = .pop
        
        marker.map = mapView
    }
    
    /*
     // MARK: - Navigation
     
     // In a storyboard-based application, you will often want to do a little preparation before navigation
     override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
     // Get the new view controller using segue.destination.
     // Pass the selected object to the new view controller.
     }
     */
    
}
