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

class ExteriorViewController: UIViewController {

    @IBOutlet weak var mPauseBtn: UIButton!
    
    @IBOutlet weak var mDonutTime: DACircularProgressView!
    
    @IBOutlet weak var mFragmentContainer: UIView!
    
    @IBOutlet weak var mScoreBar: YLProgressBar!
    
    @IBOutlet weak var mScoreText: UILabel!
    
    @IBOutlet weak var mYellowBtn: UIButton!
    
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
            controller.mIndicatorA.setIcon(icon: .googleMaterialDesign(.newReleases), iconSize: 24, color: .white, bgColor: UIColor(named: "Amber")!)
                        controller.mIndicatorB.setIcon(icon: .googleMaterialDesign(.check), iconSize: 24, color: .white, bgColor: UIColor(named: "success")!)
                        controller.mIndicatorC.setIcon(icon: .googleMaterialDesign(.close), iconSize: 24, color: .white, bgColor: UIColor(named: "danger")!)
            
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
