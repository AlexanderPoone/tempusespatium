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
import SwiftyJSON

class MapGameViewController: UIViewController, GMSMapViewDelegate {
    
    @IBOutlet weak var mInstructions: UILabel!
    
    @IBOutlet weak var mMap: GMSMapView!
    
    @IBOutlet weak var mResetBtn: UIButton!
    
    @IBOutlet weak var mSubmitBtn: UIButton!
    
    
    @IBAction func mResetBtnClicked(_ sender: Any) {
        if let marker = mMarker {
            marker.map = nil
        }
        mMap.animate(toZoom: 0)
    }
    
    var mMarker:GMSMarker?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        mResetBtn.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeSolid(.undo), iconColor: .white, postfixText: NSLocalizedString("reset", comment: ""), postfixTextColor: .white, backgroundColor: UIColor(named: "danger")!, forState: .normal, textSize: nil, iconSize: nil)
        
        mSubmitBtn.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeSolid(.paperPlane), iconColor: .white, postfixText: NSLocalizedString("submit", comment: ""), postfixTextColor: .white, backgroundColor: UIColor(named: "success")!, forState: .normal, textSize: nil, iconSize: nil)
        
        mMap.delegate = self
        mMap.settings.tiltGestures = false
        mMap.settings.rotateGestures = false
        mMap.camera = GMSCameraPosition(latitude: CLLocationDegrees(exactly: 0)!, longitude: CLLocationDegrees(exactly: 0)!, zoom: 0)
        
        mMap.mapStyle = try! GMSMapStyle(jsonString: "[{\"elementType\":\"geometry.stroke\",\"stylers\":[{\"visibility\":\"off\"}]},{\"elementType\":\"labels\",\"stylers\":[{\"visibility\":\"off\"}]}]")
        //GeoJSON
        revealAnswer("Eswatini")
    }
    
    func revealAnswer(_ state:String) {
        let polygon = GMSPolygon()
        polygon.fillColor = UIColor(named: "BlueDialogBackground")!
        polygon.strokeColor = .black
        polygon.strokeWidth = 2
        let polygonPath = GMSMutablePath()
        
        let states = Bundle.main.url(forResource: "states_of_the_world", withExtension: "geojson")!
        let jsonData = try! Data(contentsOf: states)
        let json = try! JSON(data: jsonData)
        
        let features = json["features"].filter { (arg0) -> Bool in
            
            //            let (String, JSON) = arg0
            if let countryName = arg0.1["properties"]["ADMIN"].string {
                return countryName == state
            } else {
                return false
            }
        }
        
        var avgLat:Double = 0, avgLng:Double = 0
        
        if features.count > 0 {
            let arr = features[0].1["geometry"]["coordinates"][0].arrayValue
            for x in arr {
                polygonPath.add(CLLocationCoordinate2D(latitude: CLLocationDegrees(exactly: x[1].doubleValue)!, longitude: CLLocationDegrees(exactly: x[0].doubleValue)!))
                avgLat += x[1].doubleValue
                avgLng += x[0].doubleValue
            }
            avgLat /= Double(arr.count)
            avgLng /= Double(arr.count)
            
            polygon.path = polygonPath
            polygon.map = mMap
            
            mMap.animate(toLocation: CLLocationCoordinate2D(latitude: CLLocationDegrees(exactly:avgLat)!, longitude: CLLocationDegrees(exactly:avgLng)!))
        }
        
    }
    
    func mapView(_ mapView: GMSMapView, didTapAt coordinate: CLLocationCoordinate2D) {
        mMarker = GMSMarker()
        mMarker!.isDraggable = false
        mMarker!.position = coordinate
        mMarker!.icon = UIImage(named: "rocket_pointer")!
        mMarker!.appearAnimation = .pop
        
        mMarker!.map = mapView
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
