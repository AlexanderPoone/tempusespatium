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
import OGVKit

class MapGameViewController: UIViewController, GMSMapViewDelegate {
    
    @IBOutlet weak var mInstructions: UILabel!
    
    @IBOutlet weak var mMap: GMSMapView!
    
    @IBOutlet weak var mResetBtn: UIButton!
    
    @IBOutlet weak var mSubmitBtn: UIButton!
    
    @IBOutlet weak var mAnswerLbl: UILabel!
    
    @IBAction func mResetBtnClicked(_ sender: Any) {
        if let marker = mMarker {
            marker.map = nil
        }
        mMap.animate(toZoom: 0)
    }
    
    
    var mMarker:GMSMarker?
    var mOgvPlayerView:OGVPlayerView?
    var mJson:JSON?
    
    var mCorrectAnswer:String?, mCorrectAnswerEn:String?
    
    var mPolygons:[GMSPolygon] = [], mBounds = GMSCoordinateBounds()
    
    var mPointChange = -5
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let states = Bundle.main.url(forResource: "states_of_the_world", withExtension: "geojson")!
        let jsonData = try! Data(contentsOf: states)
        mJson = try! JSON(data: jsonData)
        
        //        mOgvPlayerView = OGVPlayerView()
        //        mOgvPlayerView!.frame = .null
        //        view.addSubview(mOgvPlayerView!)
        //        mOgvPlayerView!.sourceURL = URL(string: "http://commons.wikimedia.org/wiki/Special:FilePath/United%20States%20Navy%20Band%20-%20O%20Canada.ogg")!
        //        mOgvPlayerView!.play()
        //        let ogvKit = OGVInputStream(url: URL(string: "")!)
        
        mResetBtn.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeSolid(.undo), iconColor: .white, postfixText: NSLocalizedString("reset", comment: ""), postfixTextColor: .white, backgroundColor: UIColor(named: "danger")!, forState: .normal, textSize: nil, iconSize: nil)
        
        mSubmitBtn.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeSolid(.paperPlane), iconColor: .white, postfixText: NSLocalizedString("submit", comment: ""), postfixTextColor: .white, backgroundColor: UIColor(named: "success")!, forState: .normal, textSize: nil, iconSize: nil)
        
        mMap.delegate = self
        mMap.settings.tiltGestures = false
        mMap.settings.rotateGestures = false
        mMap.camera = GMSCameraPosition(latitude: CLLocationDegrees(exactly: 0)!, longitude: CLLocationDegrees(exactly: 0)!, zoom: 0)
        
        mMap.mapStyle = try! GMSMapStyle(jsonString: "[{\"elementType\":\"geometry.stroke\",\"stylers\":[{\"visibility\":\"off\"}]},{\"elementType\":\"labels\",\"stylers\":[{\"visibility\":\"off\"}]}]")
    }
    
    override func viewWillAppear(_ animated: Bool) {
        let features = mJson!["features"].filter { (arg0) -> Bool in
            
            //            let (String, JSON) = arg0
            if let countryName = arg0.1["properties"]["ADMIN"].string {
                return countryName == mCorrectAnswerEn!
            } else {
                return false
            }
        }
        
        //        var avgLat:Double = 0, avgLng:Double = 0
        
        if features.count > 0 {
            let node = features[0].1["geometry"]
            let arrO = node["coordinates"].arrayValue
            for y in arrO {
                let polygon = GMSPolygon()
                polygon.fillColor = UIColor(named: "BlueDialogBackground")!
                polygon.strokeColor = .black
                polygon.strokeWidth = 2
                let polygonPath = GMSMutablePath()
                
                var arr:[JSON]
                if node["type"].stringValue.starts(with: "M") {
                    arr = y[0].arrayValue
                } else {
                    arr = y.arrayValue
                }
                for x in arr {
                    let coords = CLLocationCoordinate2D(latitude: CLLocationDegrees(exactly: x[1].doubleValue)!, longitude: CLLocationDegrees(exactly: x[0].doubleValue)!)
                    polygonPath.add(coords)
                    mBounds = mBounds.includingCoordinate(coords)
                    //                    avgLat += x[1].doubleValue
                    //                    avgLng += x[0].doubleValue
                }
                //                avgLat /= Double(arr.count)
                //                avgLng /= Double(arr.count)
                
                print("Test \(polygonPath)")
                polygon.path = polygonPath
                mPolygons.append(polygon)
            }
        }
    }
    
    @objc func submitMakeshift() {
        //        (parent as! Round1ViewController)
        //        reveal() //["Philippines", "Maldives", "Comoros", "France", "South Africa", "East Timor"].randomElement()!
    }
    
    
    func reveal() {
        mMap.settings.scrollGestures = false
        mMap.settings.zoomGestures = false
        
        if mPolygons.count > 0 {
            for p in mPolygons {
                p.map = mMap
            }
            
            let camUpdate = GMSCameraUpdate.fit(mBounds)
            mMap.animate(with: camUpdate)
        }
        
        if mOgvPlayerView != nil {
            print("try to pause")
            mOgvPlayerView!.pause()
            mOgvPlayerView = nil
        }
        
        if let marker = mMarker {
            let geocoder = GMSGeocoder()
            geocoder.reverseGeocodeCoordinate(marker.position) { (res, err) in
                if let res2 = res {
                    if let playerState = res2.firstResult()!.country {
                        if self.mCorrectAnswer! == playerState {
                            self.mPointChange = 5
                        }
                        self.mAnswerLbl.text = "\(NSLocalizedString("answer", comment: "")) \(self.mCorrectAnswer!)\n\(NSLocalizedString("you_chose", comment: "")) \(playerState)"
                    } else {
                        self.mAnswerLbl.text = "\(NSLocalizedString("answer", comment: "")) \(self.mCorrectAnswer!)"
                    }
                } else {
                    self.mAnswerLbl.text = "\(NSLocalizedString("answer", comment: "")) \(self.mCorrectAnswer!)"
                }
            }
        } else {
            self.mAnswerLbl.text = "\(NSLocalizedString("answer", comment: "")) \(self.mCorrectAnswer!)"
        }
        
        
    }
    
    func mapView(_ mapView: GMSMapView, didTapAt coordinate: CLLocationCoordinate2D) {
        if let marker = mMarker {
            marker.map = nil
        }
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
