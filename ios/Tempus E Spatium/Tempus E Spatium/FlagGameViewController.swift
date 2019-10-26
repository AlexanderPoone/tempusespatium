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
    var mCorrectState:String?
    
    override func viewDidLoad() {
        super.viewDidLoad()

    }
    
    override func viewWillAppear(_ animated: Bool) {
//        mQuestion.text = mQuestionStr!
    }
    
    func reveal() {
        mIndicatorA.setIcon(icon: .googleMaterialDesign(.newReleases), iconSize: 24, color: .white, bgColor: UIColor(named: "Amber")!)
        mClickAreaA.backgroundColor = UIColor(named: "Amber")!
        mIndicatorB.setIcon(icon: .googleMaterialDesign(.check), iconSize: 24, color: .white, bgColor: UIColor(named: "success")!)
        mIndicatorB.backgroundColor = UIColor(named: "success")!
        mIndicatorC.setIcon(icon: .googleMaterialDesign(.close), iconSize: 24, color: .white, bgColor: UIColor(named: "danger")!)
        mIndicatorC.backgroundColor = UIColor(named: "danger")!
    }

}
