//
//  FlagGameViewController.swift
//  Tempus E Spatium
//
//  Created by Victor Poon on 15/10/2019.
//  Copyright © 2019 SoftFeta. All rights reserved.
//

import UIKit
import SwiftIcons

class FlagGameViewController: UIViewController {
    
    @IBOutlet weak var mQuestion: UILabel!
    
    
    @IBOutlet weak var mClickAreaA: UIView!
    @IBOutlet weak var mFlagA: UIImageView!
    @IBOutlet weak var mIndicatorA: UILabel!
    
    @IBOutlet weak var mClickAreaB: UIView!
    @IBOutlet weak var mFlagB: UIImageView!
    @IBOutlet weak var mIndicatorB: UILabel!
    
    @IBOutlet weak var mClickAreaC: UIView!
    @IBOutlet weak var mFlagC: UIImageView!
    @IBOutlet weak var mIndicatorC: UILabel!
    
    @IBOutlet weak var mClickAreaD: UIView!
    @IBOutlet weak var mFlagD: UIImageView!
    @IBOutlet weak var mIndicatorD: UILabel!
    
    var mQuestionStr:String?
    var mUserAns = -1
    var mCorrectAns:Int?
    
    var mPointChange = -5

    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
//    override func viewWillAppear(_ animated: Bool) {
//        //        mQuestion.text = mQuestionStr!
//    }
    
    func reveal() {
        
        if mUserAns == mCorrectAns! {
            mPointChanged = 5
            switch mUserAns {
            case 0:
                mIndicatorA.setIcon(icon: .googleMaterialDesign(.check), iconSize: 24, color: .white, bgColor: UIColor(named: "success")!)
                mClickAreaA.backgroundColor = UIColor(named: "success")!
            case 1:
                mIndicatorB.setIcon(icon: .googleMaterialDesign(.check), iconSize: 24, color: .white, bgColor: UIColor(named: "success")!)
                mClickAreaB.backgroundColor = UIColor(named: "success")!
            case 2:
                mIndicatorC.setIcon(icon: .googleMaterialDesign(.check), iconSize: 24, color: .white, bgColor: UIColor(named: "success")!)
                mClickAreaC.backgroundColor = UIColor(named: "success")!
            case 3:
                mIndicatorD.setIcon(icon: .googleMaterialDesign(.check), iconSize: 24, color: .white, bgColor: UIColor(named: "success")!)
                mClickAreaD.backgroundColor = UIColor(named: "success")!
            default:
                break
            }
        } else {
            switch mUserAns {
            case 0:
                mIndicatorA.setIcon(icon: .googleMaterialDesign(.close), iconSize: 24, color: .white, bgColor: UIColor(named: "danger")!)
                mClickAreaA.backgroundColor = UIColor(named: "danger")!
            case 1:
                mIndicatorB.setIcon(icon: .googleMaterialDesign(.close), iconSize: 24, color: .white, bgColor: UIColor(named: "danger")!)
                mClickAreaB.backgroundColor = UIColor(named: "danger")!
            case 2:
                mIndicatorC.setIcon(icon: .googleMaterialDesign(.close), iconSize: 24, color: .white, bgColor: UIColor(named: "danger")!)
                mClickAreaC.backgroundColor = UIColor(named: "danger")!
            case 3:
                mIndicatorD.setIcon(icon: .googleMaterialDesign(.close), iconSize: 24, color: .white, bgColor: UIColor(named: "danger")!)
                mClickAreaD.backgroundColor = UIColor(named: "danger")!
            default:
                break
            }
            switch mCorrectAns! {
            case 0:
                mIndicatorA.setIcon(icon: .googleMaterialDesign(.newReleases), iconSize: 24, color: .white, bgColor: UIColor(named: "Amber")!)
                mClickAreaA.backgroundColor = UIColor(named: "Amber")!
            case 1:
                mIndicatorB.setIcon(icon: .googleMaterialDesign(.newReleases), iconSize: 24, color: .white, bgColor: UIColor(named: "Amber")!)
                mClickAreaB.backgroundColor = UIColor(named: "Amber")!
            case 2:
                mIndicatorC.setIcon(icon: .googleMaterialDesign(.newReleases), iconSize: 24, color: .white, bgColor: UIColor(named: "Amber")!)
                mClickAreaC.backgroundColor = UIColor(named: "Amber")!
            case 3:
                mIndicatorD.setIcon(icon: .googleMaterialDesign(.newReleases), iconSize: 24, color: .white, bgColor: UIColor(named: "Amber")!)
                mClickAreaD.backgroundColor = UIColor(named: "Amber")!
            default:
                break
            }
        }
    }
    
}
