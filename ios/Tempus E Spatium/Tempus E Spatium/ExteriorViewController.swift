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

class MonthPickerDelegate: NSObject, UIPickerViewDataSource, UIPickerViewDelegate {
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return (1...12).count
    }
    
    func pickerView(_ pickerView: UIPickerView, attributedTitleForRow row: Int, forComponent component: Int) -> NSAttributedString? {

        let string = String(Array(1...12)[row])
        return NSAttributedString(string: string, attributes: [NSAttributedString.Key.foregroundColor: UIColor.white])
    }
}


class ExteriorViewController: UIViewController, UIPickerViewDataSource, UIPickerViewDelegate {
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return (1912...1949).count
    }
    
    func pickerView(_ pickerView: UIPickerView, attributedTitleForRow row: Int, forComponent component: Int) -> NSAttributedString? {

        let string = String(Array(1912...1949)[row])
        return NSAttributedString(string: string, attributes: [NSAttributedString.Key.foregroundColor: UIColor.white])
    }
    
    @IBOutlet weak var mPauseBtn: UIButton!
    
    @IBOutlet weak var mDonutTime: DALabeledCircularProgressView!
    
    @IBOutlet weak var mFragmentContainer: UIView!
    
    @IBOutlet weak var mScoreBar: YLProgressBar!
        
    @IBOutlet weak var mScoreText: UILabel!

    @IBOutlet weak var mScoreChangeLbl: UILabel!
        
    @IBOutlet weak var mYellowBtn: UIButton!
    
    private let mPreferences = UserDefaults.standard
    
    @objc func aClicked() {
        print("A Clicked")
        if let view1 = mFragmentContainer.subviews.first {
            view1.removeFromSuperview()
            let controller = storyboard!.instantiateViewController(withIdentifier: "MapGameViewController") as! MapGameViewController
            controller.view.frame = mFragmentContainer.bounds
            controller.willMove(toParent: self)
            mFragmentContainer.addSubview(controller.view)
            addChild(controller)
            controller.didMove(toParent: self)
            controller.view.backgroundColor = view.backgroundColor
        }
    }
    
    let mMonthDelegate = MonthPickerDelegate() //Must be a member, since dataSource and delegate are weak
    
    @objc func bClicked() {
        print("B Clicked")
        if let view1 = mFragmentContainer.subviews.first {
            view1.removeFromSuperview()
            let controller = storyboard!.instantiateViewController(withIdentifier: "DateGameViewController") as! DateGameViewController
            controller.view.frame = mFragmentContainer.bounds
            controller.willMove(toParent: self)
            mFragmentContainer.addSubview(controller.view)
            addChild(controller)
            
            controller.didMove(toParent: self)
            controller.view.backgroundColor = view.backgroundColor
            
            //            controller.mYear = 1937
            //            controller.mMonth = 7
            //
            controller.mYearScroller.dataSource = self
            controller.mYearScroller.delegate = self
            controller.mYearScroller.selectRow((0..<controller.mYearScroller.numberOfRows(inComponent: 0)).randomElement()!, inComponent: 0, animated: false)
            
            controller.mMonthScroller.dataSource = mMonthDelegate
            controller.mMonthScroller.delegate = mMonthDelegate
            controller.mMonthScroller.selectRow((0..<controller.mMonthScroller.numberOfRows(inComponent: 0)).randomElement()!, inComponent: 0, animated: false)
            
            let url = URL(string: "https://upload.wikimedia.org/wikipedia/commons/8/85/Tank_Battle_in_Golan_Heights_-_Flickr_-_The_Central_Intelligence_Agency.jpg")
            let data = try! Data(contentsOf: url!)
            controller.mHintPic.image = UIImage(data: data)
        }
    }
    
    @objc func cClicked() {
        print("C Clicked")
        parent!.performSegue(withIdentifier: "toHighscoreInputDialog", sender: nil)
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
            let aspect = fast.frame.width / fast.frame.height
            fast.contentMode = .scaleAspectFill
            fast.frame = CGRect(x: 0, y: 0, width: 90 * aspect, height: 90)
//            print(controller.mFlagA.frame)
            controller.mFlagA.contentMode = .scaleAspectFill
            controller.mFlagA.addSubview(fast)
            
            self.mScoreBar.progress = 0.7
            self.mScoreText.text = String(format: NSLocalizedString("bar_points", comment: ""), 70)
            
        }
    }
    
    @IBAction func mPauseBtnClicked(_ sender: Any) {
        performSegue(withIdentifier: "showPauseDialog", sender: nil)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        mScoreBar.progress = 0
        mScoreBar.trackTintColor = view.backgroundColor!
        mScoreBar.stripesColor = UIColor(named: self.mPreferences.string(forKey: "PREF_PLAYER_1_THEME")!)!
        mScoreText.text = String(format: NSLocalizedString("bar_points", comment: ""), 0)
        
        mPauseBtn.setIcon(icon: .fontAwesomeSolid(.pause), iconSize: nil, color: .white, backgroundColor: UIColor(named: "info")!, forState: .normal)
        mDonutTime.setProgress(0.5, animated: true)
        mDonutTime.roundedCorners = 5
        mDonutTime.trackTintColor = UIColor(named: "BlueDialogBackground")!
        mDonutTime.progressTintColor = UIColor(named: "Liberty")!
        mDonutTime.progressLabel.text = String(12)
    }
    
}
