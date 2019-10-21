//
//  ExteriorViewController.swift
//  Tempus E Spatium
//
//  Created by Victor Poon on 18/10/2019.
//  Copyright © 2019 SoftFeta. All rights reserved.
//

import UIKit
import SwiftIcons
import YLProgressBar
import DACircularProgress
import SVGKit
import Alamofire

class ExteriorViewController: UIViewController {

    
    @IBOutlet weak var mPauseBtn: UIButton!
    
    @IBOutlet weak var mDonutTime: DACircularProgressView!
    
    @IBOutlet weak var mFragmentContainer: UIView!
    
    @IBOutlet weak var mScoreBar: YLProgressBar!
    
    @IBOutlet weak var mScoreText: UILabel!
    
    @IBOutlet weak var mYellowBtn: UIButton!
    
    @objc func aClicked() {
        print("A Clicked")
    }

    @objc func bClicked() {
        print("B Clicked")
    }
    
    @objc func cClicked() {
        print("C Clicked")
    }
    
    @objc func dClicked() {
        print("D Clicked")
    }
    
    func reveal() {
        mScoreText.text = String(format: NSLocalizedString("", comment: ""), 12)
    }
    
    @IBAction func mYellowTest(_ sender: Any) {
        if let view1 = mFragmentContainer.subviews.first {
        view1.removeFromSuperview()

            let controller = storyboard!.instantiateViewController(withIdentifier: "FlagGameViewController") as! FlagGameViewController
            controller.view.frame = mFragmentContainer.bounds
            controller.willMove(toParent: self)
            mFragmentContainer.addSubview(controller.view)
            addChild(controller)
            controller.didMove(toParent: self)
            controller.view.backgroundColor = view.backgroundColor
            
            //AlamoFire here!
            AF.request("https://en.wikipedia.org/wiki/Wikipedia:Lists_of_popular_pages_by_WikiProject")
            .validate(statusCode: 200..<300)
            //            .validate(contentType: ["application/json"])
            .responseData { response in
                switch response.result {
                case .success(let value):
                    print("asdfsuccess")
                case .failure(let error):
                    print(error)
                }
            }
            
            let aClickedList = UITapGestureRecognizer(target: self, action: #selector(aClicked))
            controller.mClickAreaA.addGestureRecognizer(aClickedList)
            
            let bClickedList = UITapGestureRecognizer(target: self, action: #selector(bClicked))
            controller.mClickAreaB.addGestureRecognizer(bClickedList)
            
            let cClickedList = UITapGestureRecognizer(target: self, action: #selector(cClicked))
            controller.mClickAreaC.addGestureRecognizer(cClickedList)
            
            let dClickedList = UITapGestureRecognizer(target: self, action: #selector(dClicked))
            controller.mClickAreaD.addGestureRecognizer(dClickedList)
            
            controller.mIndicatorA.setIcon(icon: .googleMaterialDesign(.newReleases), iconSize: 24, color: .white, bgColor: UIColor(named: "Amber")!)
                        controller.mIndicatorB.setIcon(icon: .googleMaterialDesign(.check), iconSize: 24, color: .white, bgColor: UIColor(named: "success")!)
                        controller.mIndicatorC.setIcon(icon: .googleMaterialDesign(.close), iconSize: 24, color: .white, bgColor: UIColor(named: "danger")!)
            
            var svg1:SVGKImage = SVGKImage(contentsOf: URL(string: "https://upload.wikimedia.org/wikipedia/commons/d/de/Coat_of_arms_of_Botswana.svg"))
            let fast = SVGKFastImageView(svgkImage: svg1)!
            fast.frame = controller.mFlagA.bounds
            fast.sizeToFit()
            controller.mFlagA.addSubview(fast)
            
    }
    }
    
    @IBAction func mPauseBtnClicked(_ sender: Any) {
        performSegue(withIdentifier: "showPauseDialog", sender: nil)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        mPauseBtn.setIcon(icon: .fontAwesomeSolid(.pause), iconSize: nil, color: .white, backgroundColor: UIColor(named: "info")!, forState: .normal)
        mDonutTime.setProgress(0.5, animated: true)
        mDonutTime.roundedCorners = 5
        mDonutTime.trackTintColor = UIColor(named: "info")!
        mDonutTime.progressTintColor = UIColor(named: "warning")!
        mDonutTime.largeContentTitle = "0"
    }

}
